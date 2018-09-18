package com.bewitchment.common.block.tools;

import static net.minecraft.block.BlockHorizontal.FACING;

import com.bewitchment.common.block.BlockMod;
import com.bewitchment.common.lib.LibBlockName;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Joseph on 9/16/2018.
 */


public class BlockBrazier extends BlockMod {

	private static final PropertyBool HANGING = PropertyBool.create("hanging");

	public BlockBrazier() {
		super(LibBlockName.BRAZIER, Material.IRON);
		setDefaultState(defaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(HANGING, false));
		setSound(SoundType.METAL);
		setResistance(3F);
		setHardness(3F);
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta) {
		final EnumFacing facing = EnumFacing.getHorizontal(meta & 0b11);
		final boolean hanging = ((meta >> 2) & 1)==1;
		return getDefaultState().withProperty(FACING, facing).withProperty(HANGING, hanging);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		final EnumFacing facing = state.getValue(FACING);
		final int bit = state.getValue(HANGING)?1:0;
		return facing.getHorizontalIndex() | (bit<<2);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, HANGING);
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		return super.canPlaceBlockOnSide(world, pos, side) && ((side==EnumFacing.DOWN && canHangFromAbove(world, pos)) || (side != EnumFacing.DOWN && canSitBelow(world, pos)));
	}

	private boolean canHangFromAbove(World world, BlockPos pos) {
		return world.getBlockState(pos.up()).getBlockFaceShape(world, pos.up(), EnumFacing.DOWN) == BlockFaceShape.SOLID;
	}

	private boolean canSitBelow(World world, BlockPos pos) {
		return world.getBlockState(pos.down()).getBlockFaceShape(world, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID;
	}


	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, world, pos, blockIn, fromPos);
		if (!world.isRemote) {
			boolean hang = state.getValue(HANGING);
			if (hang && !canHangFromAbove(world, pos)) {
				if (canSitBelow(world, pos)) {
					world.setBlockState(pos, state.withProperty(HANGING, false), 3);
				} else {
					world.destroyBlock(pos, true);
				}
			} else if (!hang && !canSitBelow(world, pos)) {
				if (canHangFromAbove(world, pos)) {
					world.setBlockState(pos, state.withProperty(HANGING, true), 3);
				} else {
					world.destroyBlock(pos, true);
				}
			}
		}
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		final EnumFacing enumfacing = EnumFacing.fromAngle(placer.rotationYaw);
		final boolean hang = facing==EnumFacing.DOWN;
		return this.getDefaultState().withProperty(FACING, enumfacing.getOpposite()).withProperty(HANGING, hang);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
}
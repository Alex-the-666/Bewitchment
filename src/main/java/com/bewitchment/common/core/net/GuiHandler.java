package com.bewitchment.common.core.net;

import com.bewitchment.client.gui.GuiApiary;
import com.bewitchment.client.gui.GuiDistillery;
import com.bewitchment.client.gui.GuiOven;
import com.bewitchment.client.gui.GuiTarots;
import com.bewitchment.client.gui.GuiThreadSpinner;
import com.bewitchment.common.container.ContainerApiary;
import com.bewitchment.common.container.ContainerDistillery;
import com.bewitchment.common.container.ContainerFake;
import com.bewitchment.common.container.ContainerOven;
import com.bewitchment.common.container.ContainerThreadSpinner;
import com.bewitchment.common.lib.LibGui;
import com.bewitchment.common.tile.tiles.TileEntityApiary;
import com.bewitchment.common.tile.tiles.TileEntityDistillery;
import com.bewitchment.common.tile.tiles.TileEntityOven;
import com.bewitchment.common.tile.tiles.TileEntityTarotsTable;
import com.bewitchment.common.tile.tiles.TileEntityThreadSpinner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class was created by Arekkuusu on 16/04/2017.
 * It's distributed as part of Bewitchment under
 * the MIT license.
 */
public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		switch (LibGui.values()[ID]) {
			case APIARY:
				return tile instanceof TileEntityApiary ? new ContainerApiary(player.inventory, (TileEntityApiary) tile) : null;
			case OVEN:
				return tile instanceof TileEntityOven ? new ContainerOven(player.inventory, (TileEntityOven) tile) : null;
			case THREAD_SPINNER:
				return tile instanceof TileEntityThreadSpinner ? new ContainerThreadSpinner(player.inventory, (TileEntityThreadSpinner) tile) : null;
			case TAROT:
				return new ContainerFake();// No container
			case DISTILLERY:
				return tile instanceof TileEntityDistillery ? new ContainerDistillery(player.inventory, (TileEntityDistillery) tile) : null;
			default:
				return null;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		switch (LibGui.values()[ID]) {
			case APIARY:
				return tile instanceof TileEntityApiary ? new GuiApiary(player.inventory, (TileEntityApiary) tile) : null;
			case OVEN:
				return tile instanceof TileEntityOven ? new GuiOven((ContainerOven) getServerGuiElement(ID, player, world, x, y, z), player.inventory) : null;
			case THREAD_SPINNER:
				return tile instanceof TileEntityThreadSpinner ? new GuiThreadSpinner(player.inventory, (TileEntityThreadSpinner) tile) : null;
			case TAROT:
				return tile instanceof TileEntityTarotsTable ? new GuiTarots() : null;
			case DISTILLERY:
				return tile instanceof TileEntityDistillery ?  new GuiDistillery((ContainerDistillery) getServerGuiElement(ID, player, world, x, y, z), player.inventory) : null;
			default:
				return null;
		}
	}
}

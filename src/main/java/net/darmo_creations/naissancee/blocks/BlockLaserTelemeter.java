package net.darmo_creations.naissancee.blocks;

import net.darmo_creations.naissancee.Utils;
import net.darmo_creations.naissancee.gui.GuiLaserTelemeter;
import net.darmo_creations.naissancee.tile_entities.TileEntityLaserTelemeter;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

/**
 * A block that draws lines of desired lengths to measure distances.
 *
 * @see TileEntityLaserTelemeter
 */
public class BlockLaserTelemeter extends BlockContainer implements IModBlock {
  public BlockLaserTelemeter() {
    super(Material.CIRCUITS);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    Optional<TileEntityLaserTelemeter> te = Utils.getTileEntity(TileEntityLaserTelemeter.class, world, pos);

    if (te.isPresent()) {
      if (world.isRemote) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiLaserTelemeter(te.get()));
      }
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileEntityLaserTelemeter();
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean hasComparatorInputOverride(IBlockState state) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
    return 0;
  }

  @Override
  public int quantityDropped(Random random) {
    return 0;
  }

  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.MODEL;
  }
}

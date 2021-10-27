package net.darmo_creations.naissancee.blocks;

import net.darmo_creations.naissancee.Utils;
import net.darmo_creations.naissancee.gui.GuiLightOrbController;
import net.darmo_creations.naissancee.items.ModItems;
import net.darmo_creations.naissancee.tile_entities.TileEntityLightOrbController;
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

public class BlockLightOrbController extends BlockContainer implements IModBlock {
  public BlockLightOrbController() {
    super(Material.IRON);
    this.setBlockUnbreakable();
  }

  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
    super.onBlockAdded(world, pos, state);
    Utils.getTileEntity(TileEntityLightOrbController.class, world, pos).ifPresent(TileEntityLightOrbController::init);
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {
    Utils.getTileEntity(TileEntityLightOrbController.class, world, pos).ifPresent(te -> {
      te.killOrb();
      super.breakBlock(world, pos, state);
    });
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    Optional<TileEntityLightOrbController> te = Utils.getTileEntity(TileEntityLightOrbController.class, world, pos);

    if (te.isPresent() && player.canUseCommandBlock() && player.getHeldItem(hand).getItem() == ModItems.LIGHT_ORB_TWEAKER) {
      if (world.isRemote) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiLightOrbController(te.get()));
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
    return new TileEntityLightOrbController();
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

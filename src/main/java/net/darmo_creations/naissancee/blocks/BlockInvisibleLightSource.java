package net.darmo_creations.naissancee.blocks;

import net.darmo_creations.naissancee.Utils;
import net.darmo_creations.naissancee.tile_entities.TileEntityInvisibleLightSource;
import net.darmo_creations.naissancee.tile_entities.TileEntityInvisibleLightSource.EnumMode;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Optional;

public class BlockInvisibleLightSource extends BlockVariableLamp implements ITileEntityProvider {
  public static final AxisAlignedBB AABB = new AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);

  public BlockInvisibleLightSource() {
    super(true);
    this.setLightLevel(1f);
    this.translucent = true;
  }

  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
    world.setBlockState(pos, state.withProperty(LIGHT_LEVEL, 15));
  }

  @Override
  protected void updateLightLevel(World world, BlockPos pos) {
    Optional<TileEntityInvisibleLightSource> te = Utils.getTileEntity(TileEntityInvisibleLightSource.class, world, pos);
    if (te.isPresent() && te.get().getMode() == EnumMode.REDSTONE) {
      super.updateLightLevel(world, pos);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB;
  }

  @Override
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {
    return NULL_AABB;
  }

  @SuppressWarnings("deprecation")
  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
  }

  @Override
  public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  public boolean isPassable(IBlockAccess world, BlockPos pos) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isTopSolid(IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideSolid(IBlockState baseState, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return false;
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta) {
    return new TileEntityInvisibleLightSource();
  }
}

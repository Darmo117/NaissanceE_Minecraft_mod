package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This block represents a light-sensitive block.
 */
public class BlockLightSensitiveBarrier extends Block {
  private final boolean passable;

  /**
   * Create a light sensitive barrier block.
   *
   * @param passable Whether players can pass through it.
   */
  public BlockLightSensitiveBarrier(final boolean passable) {
    super(Material.ROCK);
    this.passable = passable;
  }

  @SuppressWarnings("deprecation")
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {
    return this.passable ? NULL_AABB : FULL_BLOCK_AABB;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
    return this.passable ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isTopSolid(IBlockState state) {
    return !this.passable;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return !this.passable;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return !this.passable;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isFullCube(IBlockState state) {
    return !this.passable;
  }

  @Override
  public boolean isPassable(IBlockAccess world, BlockPos pos) {
    return this.passable;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() {
    return this.passable ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.SOLID;
  }

  @SuppressWarnings("deprecation")
  @SideOnly(Side.CLIENT)
  @Override
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return this.passable || super.shouldSideBeRendered(blockState, blockAccess, pos, side);
  }
}

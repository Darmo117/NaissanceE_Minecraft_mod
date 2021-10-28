package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This block represents a light-sensitive block found in NaissanceE.
 * <p>
 * When in complete darkness it is transparent and players can walk through it.
 * When exposed to light it becomes solid and opaque.
 */
// TODO make light-sensitive
public class BlockLightSensitiveBarrier extends Block {
  public static final PropertyBool PASSABLE = PropertyBool.create("passable");

  public BlockLightSensitiveBarrier() {
    super(Material.ROCK);
  }

  @SuppressWarnings("deprecation")
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return FULL_BLOCK_AABB;
  }

  @SuppressWarnings("deprecation")
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return blockState.getValue(PASSABLE) ? NULL_AABB : FULL_BLOCK_AABB;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
    return state.getValue(PASSABLE) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isTopSolid(IBlockState state) {
    return !state.getValue(PASSABLE);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return !state.getValue(PASSABLE);
  }

  @Override
  public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
    return !state.getValue(PASSABLE);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return !state.getValue(PASSABLE);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isFullCube(IBlockState state) {
    return !state.getValue(PASSABLE);
  }

  @Override
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos).getValue(PASSABLE);
  }

  @SuppressWarnings("deprecation")
  @Override
  @SideOnly(Side.CLIENT)
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return true;
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(PASSABLE) ? 1 : 0;
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(PASSABLE, meta != 0);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, PASSABLE);
  }
}

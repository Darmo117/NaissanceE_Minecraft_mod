package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * A small thin framed luminous block.
 */
public class BlockSmallFramedLamp extends BlockDirectional implements IModBlock {
  private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.1875, 0.1875, 0.75, 0.8125, 0.8125, 1.0);
  private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.1875, 0.1875, 0.0, 0.8125, 0.8125, 0.25);
  private static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.75, 0.1875, 0.1875, 1, 0.8125, 0.8125);
  private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0, 0.1875, 0.1875, 0.25, 0.8125, 0.8125);
  private static final AxisAlignedBB AABB_UP = new AxisAlignedBB(0.1875, 0, 0.1875, 0.8125, 0.25, 0.8125);
  private static final AxisAlignedBB AABB_DOWN = new AxisAlignedBB(0.1875, 0.75, 0.1875, 0.8125, 1, 0.8125);

  public BlockSmallFramedLamp() {
    super(Material.GLASS);
    this.setLightLevel(1);
    this.setSoundType(SoundType.GLASS);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }

  @SuppressWarnings("deprecation")
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    switch (state.getValue(FACING)) {
      case NORTH:
        return AABB_NORTH;
      case SOUTH:
        return AABB_SOUTH;
      case WEST:
        return AABB_WEST;
      case EAST:
        return AABB_EAST;
      case UP:
        return AABB_UP;
      case DOWN:
        return AABB_DOWN;
    }
    return null; // Should never happen
  }

  @Override
  public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
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

  @SuppressWarnings("deprecation")
  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, facing);
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(FACING).getIndex();
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState withMirror(IBlockState state, Mirror mirror) {
    return state.withRotation(mirror.toRotation(state.getValue(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }
}

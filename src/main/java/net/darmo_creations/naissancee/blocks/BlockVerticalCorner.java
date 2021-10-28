package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.BlockHorizontal;
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
 * A half vertical slab. It can face any of the horizontal directions.
 *
 * @see BlockVerticalSlab
 */
public class BlockVerticalCorner extends BlockHorizontal implements IModBlock {
  private static final AxisAlignedBB[] AABBS = {
      // North (North-West)
      new AxisAlignedBB(0, 0, 0, 0.5, 1, 0.5),
      // South (South-East)
      new AxisAlignedBB(0.5, 0, 0.5, 1, 1, 1),
      // West (South-West)
      new AxisAlignedBB(0, 0, 0.5, 0.5, 1, 1),
      // East (North-East)
      new AxisAlignedBB(0.5, 0, 0, 1, 1, 0.5),
  };

  public BlockVerticalCorner(Material material) {
    super(material, material.getMaterialMapColor());
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
        return AABBS[0];
      case SOUTH:
        return AABBS[1];
      case WEST:
        return AABBS[2];
      case EAST:
        return AABBS[3];
    }
    return null; // Should never happen
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isTopSolid(IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
        .withProperty(FACING, placer.getHorizontalFacing());
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
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(FACING).getHorizontalIndex();
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

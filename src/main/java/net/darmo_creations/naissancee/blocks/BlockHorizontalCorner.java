package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
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
 * A half normal slab. It can face any of the horizontal directions and be placed on the bottom or top half.
 *
 * @see BlockSlab
 */
public class BlockHorizontalCorner extends BlockHorizontal implements IModBlock {
  public static final PropertyEnum<BlockSlab.EnumBlockHalf> HALF = PropertyEnum.create("half", BlockSlab.EnumBlockHalf.class);

  private static final AxisAlignedBB[] AABBS = {
      // North bottom
      new AxisAlignedBB(0, 0, 0, 1, 0.5, 0.5),
      // South bottom
      new AxisAlignedBB(0, 0, 0.5, 1, 0.5, 1),
      // West bottom
      new AxisAlignedBB(0, 0, 0, 0.5, 0.5, 1),
      // East bottom
      new AxisAlignedBB(0.5, 0, 0, 1, 0.5, 1),
      // North top
      new AxisAlignedBB(0, 0.5, 0, 1, 1, 0.5),
      // South top
      new AxisAlignedBB(0, 0.5, 0.5, 1, 1, 1),
      // West top
      new AxisAlignedBB(0, 0.5, 0, 0.5, 1, 1),
      // East top
      new AxisAlignedBB(0.5, 0.5, 0, 1, 1, 1),
  };

  public BlockHorizontalCorner(Material material) {
    super(material, material.getMaterialMapColor());
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING, HALF);
  }

  @SuppressWarnings("deprecation")
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    if (state.getValue(HALF) == BlockSlab.EnumBlockHalf.BOTTOM) {
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
    } else {
      switch (state.getValue(FACING)) {
        case NORTH:
          return AABBS[4];
        case SOUTH:
          return AABBS[5];
        case WEST:
          return AABBS[6];
        case EAST:
          return AABBS[7];
      }
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
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState iblockstate = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer)
        .withProperty(FACING, placer.getHorizontalFacing());
    return (facing.getAxis() == EnumFacing.Axis.X || facing.getAxis() == EnumFacing.Axis.Z) && hitY <= 0.5 || facing == EnumFacing.UP
        ? iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM)
        : iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.TOP);
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

  @Override
  @SuppressWarnings("deprecation")
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState()
        .withProperty(HALF, (meta & 4) != 0 ? BlockSlab.EnumBlockHalf.TOP : BlockSlab.EnumBlockHalf.BOTTOM)
        .withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    if (state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
      i |= 4;
    }
    return i | (state.getValue(FACING).getHorizontalIndex());
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

package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.BlockLadder;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * One half of a ladder. Meant to be used as a way to center a ladder between two blocks.
 */
public class BlockHalfLadder extends BlockLadder implements IModBlock {
  public static final PropertyEnum<EnumSide> SIDE = PropertyEnum.create("side", EnumSide.class);

  protected static final AxisAlignedBB NORTH_LEFT_AABB = new AxisAlignedBB(0.5, 0, 0.8125, 1, 1, 1);
  protected static final AxisAlignedBB SOUTH_LEFT_AABB = new AxisAlignedBB(0, 0, 0, 0.5, 1, 0.1875);
  protected static final AxisAlignedBB WEST_LEFT_AABB = new AxisAlignedBB(0.8125, 0, 0, 1, 1, 0.5);
  protected static final AxisAlignedBB EAST_LEFT_AABB = new AxisAlignedBB(0, 0, 0.5, 0.1875, 1, 1);

  protected static final AxisAlignedBB NORTH_RIGHT_AABB = new AxisAlignedBB(0, 0, 0.8125, 0.5, 1, 1);
  protected static final AxisAlignedBB SOUTH_RIGHT_AABB = new AxisAlignedBB(0.5, 0, 0, 1, 1, 0.1875);
  protected static final AxisAlignedBB WEST_RIGHT_AABB = new AxisAlignedBB(0.8125, 0, 0.5, 1, 1, 1);
  protected static final AxisAlignedBB EAST_RIGHT_AABB = new AxisAlignedBB(0, 0, 0, 0.1875, 1, 0.5);

  public BlockHalfLadder() {
    // Override default state defined in BlockLadder
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(FACING, EnumFacing.NORTH)
        .withProperty(SIDE, EnumSide.LEFT));
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    boolean left = state.getValue(SIDE) == EnumSide.LEFT;
    switch (state.getValue(FACING)) {
      case NORTH:
        return left ? NORTH_LEFT_AABB : NORTH_RIGHT_AABB;
      case SOUTH:
        return left ? SOUTH_LEFT_AABB : SOUTH_RIGHT_AABB;
      case WEST:
        return left ? WEST_LEFT_AABB : WEST_RIGHT_AABB;
      case EAST:
      default:
        return left ? EAST_LEFT_AABB : EAST_RIGHT_AABB;
    }
  }

  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer);
    EnumFacing face = state.getValue(FACING);
    EnumSide side;

    if (face == EnumFacing.NORTH && hitX < 0.5
        || face == EnumFacing.SOUTH && hitX > 0.5
        || face == EnumFacing.EAST && hitZ < 0.5
        || face == EnumFacing.WEST && hitZ > 0.5) {
      side = EnumSide.RIGHT;
    } else {
      side = EnumSide.LEFT;
    }

    return state.withProperty(SIDE, side);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return super.getMetaFromState(state) | (state.getValue(SIDE) == EnumSide.LEFT ? 0 : 8);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return super.getStateFromMeta(meta & 7).withProperty(SIDE, (meta & 8) == 0 ? EnumSide.LEFT : EnumSide.RIGHT);
  }

  @Override
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }

  @Override
  public IBlockState withMirror(IBlockState state, Mirror mirror) {
    return state.withRotation(mirror.toRotation(state.getValue(FACING)));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING, SIDE);
  }

  /**
   * Enumeration defining the two possible sides a half ladder can be on a block’s side.
   */
  public enum EnumSide implements IStringSerializable {
    LEFT,
    RIGHT;

    @Override
    public String toString() {
      return this.getName();
    }

    @Override
    public String getName() {
      return this == LEFT ? "left" : "right";
    }
  }
}

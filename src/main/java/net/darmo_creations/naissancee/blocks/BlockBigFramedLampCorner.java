package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * A corner of a big framed lamp block.
 */
public class BlockBigFramedLampCorner extends BlockDirectional implements IModBlock {
  private static final double UNIT = 0.375;
  private static final double D_UNIT = 2 * UNIT;

  // Bounding boxes
  private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0, 0, 1 - D_UNIT, 1, 1, 1);
  private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0, 0, 0, 1, 1, D_UNIT);
  private static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(1 - D_UNIT, 0, 0, 1, 1, 1);
  private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0, 0, 0, D_UNIT, 1, 1);
  private static final AxisAlignedBB AABB_DOWN = new AxisAlignedBB(0, 1 - D_UNIT, 0, 1, 1, 1);
  private static final AxisAlignedBB AABB_UP = new AxisAlignedBB(0, 0, 0, 1, D_UNIT, 1);

  // Collision boxes (base)
  private static final AxisAlignedBB AABB_NORTH_BASE = new AxisAlignedBB(0, 0, 1 - UNIT, 1, 1, 1);
  private static final AxisAlignedBB AABB_SOUTH_BASE = new AxisAlignedBB(0, 0, 0, 1, 1, UNIT);
  private static final AxisAlignedBB AABB_WEST_BASE = new AxisAlignedBB(1 - UNIT, 0, 0, 1, 1, 1);
  private static final AxisAlignedBB AABB_EAST_BASE = new AxisAlignedBB(0, 0, 0, UNIT, 1, 1);
  private static final AxisAlignedBB AABB_DOWN_BASE = new AxisAlignedBB(0, 1 - UNIT, 0, 1, 1, 1);
  private static final AxisAlignedBB AABB_UP_BASE = new AxisAlignedBB(0, 0, 0, 1, UNIT, 1);

  // Collision boxes (lamp)
  private static final AxisAlignedBB AABB_NORTH_TL_LAMP = new AxisAlignedBB(0, 0, 1 - D_UNIT, 1 - UNIT, 1 - UNIT, 1);
  private static final AxisAlignedBB AABB_NORTH_TR_LAMP = new AxisAlignedBB(UNIT, 0, 1 - D_UNIT, 1, 1 - UNIT, 1);
  private static final AxisAlignedBB AABB_NORTH_BR_LAMP = new AxisAlignedBB(UNIT, UNIT, 1 - D_UNIT, 1, 1, 1);
  private static final AxisAlignedBB AABB_NORTH_BL_LAMP = new AxisAlignedBB(0, UNIT, 1 - D_UNIT, 1 - UNIT, 1, 1);

  private static final AxisAlignedBB AABB_SOUTH_TL_LAMP = new AxisAlignedBB(UNIT, 0, 0, 1, 1 - UNIT, D_UNIT);
  private static final AxisAlignedBB AABB_SOUTH_TR_LAMP = new AxisAlignedBB(0, 0, 0, 1 - UNIT, 1 - UNIT, D_UNIT);
  private static final AxisAlignedBB AABB_SOUTH_BR_LAMP = new AxisAlignedBB(0, UNIT, 0, 1 - UNIT, 1, D_UNIT);
  private static final AxisAlignedBB AABB_SOUTH_BL_LAMP = new AxisAlignedBB(UNIT, UNIT, 0, 1, 1, D_UNIT);

  private static final AxisAlignedBB AABB_WEST_TL_LAMP = new AxisAlignedBB(1 - D_UNIT, 0, UNIT, 1, 1 - UNIT, 1);
  private static final AxisAlignedBB AABB_WEST_TR_LAMP = new AxisAlignedBB(1 - D_UNIT, 0, 0, 1, 1 - UNIT, 1 - UNIT);
  private static final AxisAlignedBB AABB_WEST_BR_LAMP = new AxisAlignedBB(1 - D_UNIT, UNIT, 0, 1, 1, 1 - UNIT);
  private static final AxisAlignedBB AABB_WEST_BL_LAMP = new AxisAlignedBB(1 - D_UNIT, UNIT, UNIT, 1, 1, 1);

  private static final AxisAlignedBB AABB_EAST_TL_LAMP = new AxisAlignedBB(UNIT, 0, 0, D_UNIT, 1 - UNIT, 1 - UNIT);
  private static final AxisAlignedBB AABB_EAST_TR_LAMP = new AxisAlignedBB(UNIT, 0, UNIT, D_UNIT, 1 - UNIT, 1);
  private static final AxisAlignedBB AABB_EAST_BR_LAMP = new AxisAlignedBB(UNIT, UNIT, UNIT, D_UNIT, 1, 1);
  private static final AxisAlignedBB AABB_EAST_BL_LAMP = new AxisAlignedBB(UNIT, UNIT, 0, D_UNIT, 1, 1 - UNIT);

  private static final AxisAlignedBB AABB_UP_TL_LAMP = new AxisAlignedBB(0, 0, 0, 1 - UNIT, D_UNIT, 1 - UNIT);
  private static final AxisAlignedBB AABB_UP_TR_LAMP = new AxisAlignedBB(0, 0, UNIT, 1 - UNIT, D_UNIT, 1);
  private static final AxisAlignedBB AABB_UP_BR_LAMP = new AxisAlignedBB(UNIT, 0, UNIT, 1, D_UNIT, 1);
  private static final AxisAlignedBB AABB_UP_BL_LAMP = new AxisAlignedBB(UNIT, 0, 0, 1, D_UNIT, 1 - UNIT);

  private static final AxisAlignedBB AABB_DOWN_TL_LAMP = new AxisAlignedBB(0, 1 - D_UNIT, UNIT, 1 - UNIT, 1, 1);
  private static final AxisAlignedBB AABB_DOWN_TR_LAMP = new AxisAlignedBB(UNIT, 1 - D_UNIT, UNIT, 1, D_UNIT, 1);
  private static final AxisAlignedBB AABB_DOWN_BR_LAMP = new AxisAlignedBB(UNIT, 1 - D_UNIT, 0, 1, D_UNIT, 1 - UNIT);
  private static final AxisAlignedBB AABB_DOWN_BL_LAMP = new AxisAlignedBB(0, 1 - D_UNIT, 0, 1 - UNIT, 1, 1 - UNIT);

  private final EnumCorner corner;

  public BlockBigFramedLampCorner(final EnumCorner corner) {
    super(Material.GLASS);
    this.corner = corner;
    this.setLightLevel(1);
    this.setSoundType(SoundType.GLASS);
  }

  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
        .withProperty(FACING, facing);
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

  @SuppressWarnings("deprecation")
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return NULL_AABB;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity, boolean isActualState) {
    switch (state.getValue(FACING)) {
      case NORTH:
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_NORTH_BASE);
        switch (corner) {
          case TOP_LEFT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_NORTH_TL_LAMP);
            break;
          case TOP_RIGHT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_NORTH_TR_LAMP);
            break;
          case BOTTOM_RIGHT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_NORTH_BR_LAMP);
            break;
          case BOTTOM_LEFT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_NORTH_BL_LAMP);
            break;
        }
        break;
      case SOUTH:
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SOUTH_BASE);
        switch (corner) {
          case TOP_LEFT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SOUTH_TL_LAMP);
            break;
          case TOP_RIGHT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SOUTH_TR_LAMP);
            break;
          case BOTTOM_RIGHT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SOUTH_BR_LAMP);
            break;
          case BOTTOM_LEFT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SOUTH_BL_LAMP);
            break;
        }
        break;
      case WEST:
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WEST_BASE);
        switch (corner) {
          case TOP_LEFT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WEST_TL_LAMP);
            break;
          case TOP_RIGHT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WEST_TR_LAMP);
            break;
          case BOTTOM_RIGHT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WEST_BR_LAMP);
            break;
          case BOTTOM_LEFT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WEST_BL_LAMP);
            break;
        }
        break;
      case EAST:
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_EAST_BASE);
        switch (corner) {
          case TOP_LEFT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_EAST_TL_LAMP);
            break;
          case TOP_RIGHT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_EAST_TR_LAMP);
            break;
          case BOTTOM_RIGHT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_EAST_BR_LAMP);
            break;
          case BOTTOM_LEFT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_EAST_BL_LAMP);
            break;
        }
        break;
      case UP:
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_UP_BASE);
        switch (corner) {
          case TOP_LEFT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_UP_TL_LAMP);
            break;
          case TOP_RIGHT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_UP_TR_LAMP);
            break;
          case BOTTOM_RIGHT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_UP_BR_LAMP);
            break;
          case BOTTOM_LEFT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_UP_BL_LAMP);
            break;
        }
        break;
      case DOWN:
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_DOWN_BASE);
        switch (corner) {
          case TOP_LEFT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_DOWN_TL_LAMP);
            break;
          case TOP_RIGHT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_DOWN_TR_LAMP);
            break;
          case BOTTOM_RIGHT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_DOWN_BR_LAMP);
            break;
          case BOTTOM_LEFT:
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_DOWN_BL_LAMP);
            break;
        }
        break;
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
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
  public boolean isTopSolid(IBlockState state) {
    return state.getValue(FACING) == EnumFacing.DOWN;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideSolid(IBlockState baseState, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return side == baseState.getValue(FACING).getOpposite();
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(FACING).getIndex();
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }

  public enum EnumCorner implements IStringSerializable {
    TOP_LEFT("top_left"),
    TOP_RIGHT("top_right"),
    BOTTOM_RIGHT("bottom_right"),
    BOTTOM_LEFT("botton_left");

    private final String name;

    EnumCorner(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return this.name;
    }

    @Override
    public String toString() {
      return this.getName();
    }
  }
}

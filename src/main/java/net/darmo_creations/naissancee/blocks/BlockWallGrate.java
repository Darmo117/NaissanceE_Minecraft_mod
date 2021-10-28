package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashMap;
import java.util.Map;

/**
 * A grate that is embedded into a wall.
 */
public class BlockWallGrate extends AbstractBlockGrate {
  private static final Map<ImmutablePair<EnumFacing, EnumDirection>, AxisAlignedBB> AABBS;

  static {
    AABBS = new HashMap<>();
    AABBS.put(new ImmutablePair<>(EnumFacing.NORTH, EnumDirection.LEFT_RIGHT),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
    AABBS.put(new ImmutablePair<>(EnumFacing.SOUTH, EnumDirection.LEFT_RIGHT),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
    AABBS.put(new ImmutablePair<>(EnumFacing.WEST, EnumDirection.LEFT_RIGHT),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
    AABBS.put(new ImmutablePair<>(EnumFacing.EAST, EnumDirection.LEFT_RIGHT),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
    AABBS.put(new ImmutablePair<>(EnumFacing.NORTH, EnumDirection.RIGHT_LEFT),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
    AABBS.put(new ImmutablePair<>(EnumFacing.SOUTH, EnumDirection.RIGHT_LEFT),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
    AABBS.put(new ImmutablePair<>(EnumFacing.WEST, EnumDirection.RIGHT_LEFT),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
    AABBS.put(new ImmutablePair<>(EnumFacing.EAST, EnumDirection.RIGHT_LEFT),
        new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
  }

  public BlockWallGrate(Material material) {
    super(material, material.getMaterialMapColor());
  }

  @Override
  protected AxisAlignedBB getBoundingBox(EnumFacing facing, EnumDirection direction) {
    return AABBS.get(new ImmutablePair<>(facing, direction));
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    EnumFacing face = base_state.getValue(FACING);

    if (base_state.getValue(DIRECTION) == EnumDirection.LEFT_RIGHT) {
      switch (side) {
        case NORTH:
          if (face == EnumFacing.WEST || face == EnumFacing.SOUTH) {
            return true;
          }
          break;
        case SOUTH:
          if (face == EnumFacing.EAST || face == EnumFacing.NORTH) {
            return true;
          }
          break;
        case WEST:
          if (face == EnumFacing.EAST || face == EnumFacing.SOUTH) {
            return true;
          }
          break;
        case EAST:
          if (face == EnumFacing.WEST || face == EnumFacing.NORTH) {
            return true;
          }
          break;
      }
    } else {
      switch (side) {
        case NORTH:
          if (face == EnumFacing.EAST || face == EnumFacing.SOUTH) {
            return true;
          }
          break;
        case SOUTH:
          if (face == EnumFacing.WEST || face == EnumFacing.NORTH) {
            return true;
          }
          break;
        case WEST:
          if (face == EnumFacing.EAST || face == EnumFacing.NORTH) {
            return true;
          }
          break;
        case EAST:
          if (face == EnumFacing.WEST || face == EnumFacing.SOUTH) {
            return true;
          }
          break;
      }
    }

    return false;
  }
}

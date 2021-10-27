package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockWallGrate extends AbstractBlockGrate {
  // TODO affiner l’AABB
  public static final AxisAlignedBB[] AABBS = {
      // North LR
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
      // South LR
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
      // West LR
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
      // East LR
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
      // North RL
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
      // South RL
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
      // West RL
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
      // East RL
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0),
  };

  public BlockWallGrate(Material material) {
    super(material, material.getMaterialMapColor(), AABBS);
  }

  @SuppressWarnings("unused")
  public BlockWallGrate(Material material, MapColor colorIn) {
    super(material, colorIn, AABBS);
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

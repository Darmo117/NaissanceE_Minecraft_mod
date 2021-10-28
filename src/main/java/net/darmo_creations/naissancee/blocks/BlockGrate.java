package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * A see-through grate.
 */
public class BlockGrate extends AbstractBlockGrate {
  // TODO refine AABB
  private static final AxisAlignedBB[] AABBS = {
      // North LR
      new AxisAlignedBB(0.25, 0.0, 0.0, 1.0, 1.0, 0.5),
      // South LR
      new AxisAlignedBB(0.0, 0.0, 0.5, 0.75, 1.0, 1.0),
      // West LR
      new AxisAlignedBB(0.0, 0.0, 0.0, 0.5, 1.0, 0.75),
      // East LR
      new AxisAlignedBB(0.5, 0.0, 0.25, 1.0, 1.0, 1.0),
      // North RL
      new AxisAlignedBB(0.0, 0.0, 0.0, 0.75, 1.0, 0.5),
      // South RL
      new AxisAlignedBB(0.25, 0.0, 0.5, 1.0, 1.0, 1.0),
      // West RL
      new AxisAlignedBB(0.0, 0.0, 0.25, 0.5, 1.0, 1.0),
      // East RL
      new AxisAlignedBB(0.5, 0.0, 0.0, 1.0, 1.0, 0.75),
  };

  public BlockGrate(Material material) {
    super(material, material.getMaterialMapColor(), AABBS);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return false;
  }
}

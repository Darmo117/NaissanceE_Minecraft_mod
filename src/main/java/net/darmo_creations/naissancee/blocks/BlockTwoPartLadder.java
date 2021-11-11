package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Custom ladder that does not disappear if the block it is placed against is removed.
 */
public class BlockTwoPartLadder extends BlockLadder implements IModBlock {
  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
    EnumFacing enumfacing = state.getValue(FACING);

    if (!this.isPistonPart(world, pos.offset(enumfacing.getOpposite()))) {
      this.dropBlockAsItem(world, pos, state, 0);
      world.setBlockToAir(pos);
    }
  }

  private boolean isPistonPart(World world, BlockPos pos) {
    return !isExceptBlockForAttachWithPiston(world.getBlockState(pos).getBlock());
  }
}

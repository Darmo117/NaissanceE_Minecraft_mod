package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Random;

/**
 * A block whose light level changes depending on incoming redstone power.
 */
public class BlockVariableLamp extends BlockVariableLightSource implements IModBlock {
  public BlockVariableLamp() {
    super(Material.ROCK);
  }

  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
    this.updateLightLevel(world, pos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
    this.updateLightLevel(world, pos);
  }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    this.updateLightLevel(world, pos);
  }

  /**
   * Update the light level of the block at the given position.
   * The block’s light level is given by the maximum incoming redstone power.
   */
  protected void updateLightLevel(World world, BlockPos pos) {
    if (!world.isRemote) {
      int level = this.getRedstonePower(world, pos);
      world.setBlockState(pos, this.getDefaultState().withProperty(LIGHT_LEVEL, level), 2);
    }
  }

  /**
   * Return the maximum incoming redstone power for the given position.
   */
  protected int getRedstonePower(World world, BlockPos pos) {
    return Arrays.stream(EnumFacing.values())
        .mapToInt(facing -> world.getRedstonePower(pos.offset(facing), facing))
        .max()
        .orElse(0);
  }
}

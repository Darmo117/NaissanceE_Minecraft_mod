package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Random;

public class BlockVariableLamp extends BlockVariableLightSource implements IModBlock {
  private final boolean useRedstone;

  public BlockVariableLamp(boolean useRedstone) {
    super(Material.ROCK);
    this.useRedstone = useRedstone;
  }

  @Override
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    if (this.useRedstone) {
      this.updateLightLevel(worldIn, pos);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    if (this.useRedstone) {
      this.updateLightLevel(worldIn, pos);
    }
  }

  @Override
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (this.useRedstone) {
      this.updateLightLevel(worldIn, pos);
    }
  }

  protected void updateLightLevel(World world, BlockPos pos) {
    if (!world.isRemote) {
      int level = this.getRedstonePower(world, pos);
      world.setBlockState(pos, this.getDefaultState().withProperty(LIGHT_LEVEL, level), 2);
    }
  }

  protected int getRedstonePower(World world, BlockPos pos) {
    return Arrays.stream(EnumFacing.values())
        .mapToInt(facing -> world.getRedstonePower(pos.offset(facing), facing))
        .max()
        .orElse(0);
  }
}

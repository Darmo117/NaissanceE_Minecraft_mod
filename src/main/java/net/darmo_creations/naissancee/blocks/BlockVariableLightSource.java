package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Base class for blocks that have 16 different light levels.
 */
public abstract class BlockVariableLightSource extends Block implements IModBlock {
  public static final PropertyInteger LIGHT_LEVEL = PropertyInteger.create("light_level", 0, 15);

  public BlockVariableLightSource(Material material) {
    super(material);
  }

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
    return state.getValue(LIGHT_LEVEL);
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(LIGHT_LEVEL, meta);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(LIGHT_LEVEL);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, LIGHT_LEVEL);
  }
}

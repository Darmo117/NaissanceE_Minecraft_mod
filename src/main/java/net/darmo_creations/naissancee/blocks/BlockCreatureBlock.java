package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This block represents a part of the kind of luminous “living” creatures/blobs found throughout NaissanceE.
 */
public class BlockCreatureBlock extends BlockVariableLightSource {
  public BlockCreatureBlock() {
    super(Material.ROCK);
  }

  @Override
  public boolean hasGeneratedItemBlock() {
    return false;
  }

  @Override
  public int damageDropped(IBlockState state) {
    return state.getValue(LIGHT_LEVEL);
  }

  @Override
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
    for (int i : LIGHT_LEVEL.getAllowedValues()) {
      items.add(new ItemStack(this, 1, i));
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(this, 1, state.getValue(LIGHT_LEVEL));
  }
}

package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCreature extends BlockVariableLamp {
  public BlockCreature() {
    super(false);
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

  @Override
  @SuppressWarnings("deprecation")
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(this, 1, state.getValue(LIGHT_LEVEL));
  }

  public String getUnlocalizedName(int meta) {
    return super.getUnlocalizedName() + "." + meta;
  }
}

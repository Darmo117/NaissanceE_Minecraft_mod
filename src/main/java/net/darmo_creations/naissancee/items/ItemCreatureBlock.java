package net.darmo_creations.naissancee.items;

import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Item for {@link ModBlocks#CREATURE_BLOCK}.
 */
public class ItemCreatureBlock extends ItemBlock {
  public ItemCreatureBlock() {
    super(ModBlocks.CREATURE_BLOCK);
    this.setMaxDamage(0);
    this.setHasSubtypes(true);
  }

  @Override
  public int getMetadata(int damage) {
    return damage;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {
    return this.block.getUnlocalizedName() + "." + stack.getMetadata();
  }

  @Override
  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
    if (this.isInCreativeTab(tab)) {
      for (int i = 0; i < 16; ++i) {
        items.add(new ItemStack(this, 1, i));
      }
    }
  }
}

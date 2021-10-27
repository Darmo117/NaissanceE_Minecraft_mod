package net.darmo_creations.naissancee.gui;

import net.darmo_creations.naissancee.NaissanceE;
import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.darmo_creations.naissancee.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NaissanceETab extends CreativeTabs {
  public NaissanceETab() {
    super(NaissanceE.MODID);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public ItemStack getTabIconItem() {
    return new ItemStack(ModItems.ITEM_BLOCKS.get(ModBlocks.LIVING_BLOCK));
  }
}

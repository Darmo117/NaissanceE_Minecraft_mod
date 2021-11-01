package net.darmo_creations.naissancee.items;

import net.darmo_creations.naissancee.NaissanceE;
import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.*;

/**
 * This class declares all items for this mod.
 */
@SuppressWarnings("unused")
public final class ModItems {
  public static final Item CREATURE_BLOCK = new ItemCreatureBlock().setRegistryName("creature_block").setCreativeTab(NaissanceE.CREATIVE_TAB);

  // For tile entity render only
  public static final Item INVISIBLE_LIGHT_SOURCE_REDSTONE = new Item().setRegistryName("invisible_light_source_redstone");
  // For tile entity render only
  public static final Item INVISIBLE_LIGHT_SOURCE_LOCKED = new Item().setRegistryName("invisible_light_source_locked");

  public static final Item INVISIBLE_LIGHT_SOURCE_EDITING_TOOL = new ItemInvisibleLightSourceTweaker().setRegistryName("invisible_light_source_editing_tool").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Item LIGHT_ORB_TWEAKER = new ItemLightOrbTweaker().setRegistryName("light_orb_tweaker").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Item CREATIVE_WAND = new ItemCreativeWand().setRegistryName("creative_wand").setCreativeTab(NaissanceE.CREATIVE_TAB);

  public static final Item LIGHT_GRAY_FRAMED_DOOR = new ItemFramedDoor(ModBlocks.LIGHT_GRAY_FRAMED_DOOR).setRegistryName("light_gray_framed_door").setCreativeTab(NaissanceE.CREATIVE_TAB);

  /**
   * The list of all explicitly declared items for this mod.
   */
  public static final List<Item> ITEMS = new LinkedList<>();
  /**
   * The list of all generated items for this mod’s blocks.
   */
  public static final Map<Block, ItemBlock> ITEM_BLOCKS = new HashMap<>();

  static {
    Arrays.stream(ModItems.class.getDeclaredFields())
        .filter(field -> Item.class.isAssignableFrom(field.getType()))
        .map(field -> {
          Item item;
          try {
            item = (Item) field.get(null);
          } catch (IllegalAccessException e) {
            // Should never happen
            throw new RuntimeException(e);
          }
          //noinspection ConstantConditions
          return item.setUnlocalizedName(NaissanceE.MODID + "." + item.getRegistryName().getResourcePath());
        })
        .forEach(ITEMS::add);
  }

  private ModItems() {
  }
}

package net.darmo_creations.naissancee.blocks;

import net.darmo_creations.naissancee.NaissanceE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class declares all blocks for this mod.
 */
@SuppressWarnings("unused")
public final class ModBlocks {
  public static final Block WHITE_WALL_GRATE = new BlockWallGrate(Material.ROCK).setRegistryName("white_wall_grate").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block LIGHT_WALL_GRAY_GRATE = new BlockWallGrate(Material.ROCK).setRegistryName("light_gray_wall_grate").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block GRAY_WALL_GRATE = new BlockWallGrate(Material.ROCK).setRegistryName("gray_wall_grate").setCreativeTab(NaissanceE.CREATIVE_TAB);

  public static final Block WHITE_GRATE = new BlockGrate(Material.ROCK).setRegistryName("white_grate").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block LIGHT_GRAY_GRATE = new BlockGrate(Material.ROCK).setRegistryName("light_gray_grate").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block GRAY_GRATE = new BlockGrate(Material.ROCK).setRegistryName("gray_grate").setCreativeTab(NaissanceE.CREATIVE_TAB);

  public static final Block WHITE_VERTICAL_SLAB = new BlockVerticalSlab(Material.ROCK).setRegistryName("white_vertical_slab").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block LIGHT_GRAY_VERTICAL_SLAB = new BlockVerticalSlab(Material.ROCK).setRegistryName("light_gray_vertical_slab").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block GRAY_VERTICAL_SLAB = new BlockVerticalSlab(Material.ROCK).setRegistryName("gray_vertical_slab").setCreativeTab(NaissanceE.CREATIVE_TAB);

  public static final Block WHITE_HORIZONTAL_CORNER = new BlockHorizontalCorner(Material.ROCK).setRegistryName("white_horizontal_corner").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block LIGHT_GRAY_HORIZONTAL_CORNER = new BlockHorizontalCorner(Material.ROCK).setRegistryName("light_gray_horizontal_corner").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block GRAY_VERTICAL_HORIZONTAL_CORNER = new BlockHorizontalCorner(Material.ROCK).setRegistryName("gray_horizontal_corner").setCreativeTab(NaissanceE.CREATIVE_TAB);

  public static final Block WHITE_VERTICAL_CORNER = new BlockVerticalCorner(Material.ROCK).setRegistryName("white_vertical_corner").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block LIGHT_GRAY_VERTICAL_CORNER = new BlockVerticalCorner(Material.ROCK).setRegistryName("light_gray_vertical_corner").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block GRAY_VERTICAL_VERTICAL_CORNER = new BlockVerticalCorner(Material.ROCK).setRegistryName("gray_vertical_corner").setCreativeTab(NaissanceE.CREATIVE_TAB);

  public static final Block WHITE_LIGHT_SENSITIVE_BARRIER = new BlockLightSensitiveBarrier().setRegistryName("white_light_sensitive_barrier").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block WHITE_LIGHT_SENSITIVE_BARRIER_PASSABLE = new BlockLightSensitiveBarrierPassable().setRegistryName("white_light_sensitive_barrier_passable").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block LIGHT_GRAY_LIGHT_SENSITIVE_BARRIER = new BlockLightSensitiveBarrier().setRegistryName("light_gray_light_sensitive_barrier").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block LIGHT_GRAY_LIGHT_SENSITIVE_BARRIER_PASSABLE = new BlockLightSensitiveBarrierPassable().setRegistryName("light_gray_light_sensitive_barrier_passable").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block GRAY_LIGHT_SENSITIVE_BARRIER = new BlockLightSensitiveBarrier().setRegistryName("gray_light_sensitive_barrier").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block GRAY_LIGHT_SENSITIVE_BARRIER_PASSABLE = new BlockLightSensitiveBarrierPassable().setRegistryName("gray_light_sensitive_barrier_passable").setCreativeTab(NaissanceE.CREATIVE_TAB);

  public static final Block OFFSET_WALL_LAMP = new BlockOffsetWallLamp().setRegistryName("offset_wall_lamp").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block LIGHT_GRAY_SMALL_FRAMED_LAMP = new BlockSmallFramedLamp().setRegistryName("light_gray_small_framed_lamp").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block LIGHT_GRAY_BIG_FRAMED_LAMP_TL_CORNER = new BlockBigFramedLampCorner(BlockBigFramedLampCorner.EnumCorner.TOP_LEFT).setRegistryName("light_gray_big_framed_lamp_top_left_corner").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block LIGHT_GRAY_BIG_FRAMED_LAMP_TR_CORNER = new BlockBigFramedLampCorner(BlockBigFramedLampCorner.EnumCorner.TOP_RIGHT).setRegistryName("light_gray_big_framed_lamp_top_right_corner").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block LIGHT_GRAY_BIG_FRAMED_LAMP_BR_CORNER = new BlockBigFramedLampCorner(BlockBigFramedLampCorner.EnumCorner.BOTTOM_RIGHT).setRegistryName("light_gray_big_framed_lamp_bottom_right_corner").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block LIGHT_GRAY_BIG_FRAMED_LAMP_BL_CORNER = new BlockBigFramedLampCorner(BlockBigFramedLampCorner.EnumCorner.BOTTOM_LEFT).setRegistryName("light_gray_big_framed_lamp_bottom_left_corner").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block WALL_LAMPS = new BlockWallLamps().setRegistryName("wall_lamps").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block INVISIBLE_LIGHT_SOURCE = new BlockInvisibleLightSource().setRegistryName("invisible_light_source").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block VARIABLE_LAMP = new BlockVariableLamp().setRegistryName("variable_lamp").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block ACTIVATOR_LAMP = new BlockActivatorLamp().setRegistryName("activator_lamp").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block FLOATING_VARIABLE_LIGHT_BLOCK = new BlockFloatingVariableLightBlock().setRegistryName("floating_variable_light_block").setCreativeTab(NaissanceE.CREATIVE_TAB);

  public static final Block CREATURE_BLOCK = new BlockCreatureBlock().setRegistryName("creature_block").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block LIVING_BLOCK = new BlockLivingBlock().setRegistryName("living_block").setCreativeTab(NaissanceE.CREATIVE_TAB);

  public static final Block LIGHT_ORB_SOURCE = new BlockLightOrbSource().setRegistryName("light_orb_source");
  public static final Block LIGHT_ORB_CONTROLLER = new BlockLightOrbController().setRegistryName("light_orb_controller").setCreativeTab(NaissanceE.CREATIVE_TAB);

  public static final Block LIGHT_GRAY_FRAMED_DOOR = new BlockFramedDoor().setRegistryName("light_gray_framed_door").setCreativeTab(NaissanceE.CREATIVE_TAB);

  public static final Block LIGHT_GRAY_LADDER = new BlockTwoPartLadder().setRegistryName("light_gray_ladder").setCreativeTab(NaissanceE.CREATIVE_TAB);
  public static final Block LIGHT_GRAY_HALF_LADDER = new BlockHalfLadder().setRegistryName("light_gray_half_ladder").setCreativeTab(NaissanceE.CREATIVE_TAB);

  /**
   * The list of all declared blocks for this mod.
   */
  public static final List<Block> BLOCKS = new LinkedList<>();

  static {
    Arrays.stream(ModBlocks.class.getDeclaredFields())
        .filter(field -> Block.class.isAssignableFrom(field.getType()))
        .map(field -> {
          Block block;
          try {
            block = (Block) field.get(null);
          } catch (IllegalAccessException e) {
            // Should never happen
            throw new RuntimeException(e);
          }
          //noinspection ConstantConditions
          return block.setUnlocalizedName(NaissanceE.MODID + "." + block.getRegistryName().getResourcePath());
        })
        .forEach(BLOCKS::add);

    MinecraftForge.EVENT_BUS.register(INVISIBLE_LIGHT_SOURCE);
  }

  private ModBlocks() {
  }
}

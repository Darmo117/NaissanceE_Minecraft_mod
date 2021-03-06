package net.darmo_creations.naissancee;

import net.darmo_creations.naissancee.blocks.IModBlock;
import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.darmo_creations.naissancee.calculator.CalculatorsManager;
import net.darmo_creations.naissancee.commands.CalculatorCommand;
import net.darmo_creations.naissancee.commands.ToDoListCommand;
import net.darmo_creations.naissancee.entities.EntityLightOrb;
import net.darmo_creations.naissancee.entities.EntityPlayerPusher;
import net.darmo_creations.naissancee.entities.ModEntities;
import net.darmo_creations.naissancee.entities.render.RenderLightOrb;
import net.darmo_creations.naissancee.entities.render.RenderPlayerPusher;
import net.darmo_creations.naissancee.gui.GuiToDoList;
import net.darmo_creations.naissancee.gui.NaissanceETab;
import net.darmo_creations.naissancee.items.ModItems;
import net.darmo_creations.naissancee.network.PacketLaserTelemeterData;
import net.darmo_creations.naissancee.network.PacketLightOrbControllerData;
import net.darmo_creations.naissancee.tile_entities.TileEntityFloatingVariableLightBlock;
import net.darmo_creations.naissancee.tile_entities.TileEntityInvisibleLightSource;
import net.darmo_creations.naissancee.tile_entities.TileEntityLaserTelemeter;
import net.darmo_creations.naissancee.tile_entities.TileEntityLightOrbController;
import net.darmo_creations.naissancee.tile_entities.render.TileEntityInvisibleLightSourceRenderer;
import net.darmo_creations.naissancee.tile_entities.render.TileEntityLaserTelemeterRenderer;
import net.darmo_creations.naissancee.tile_entities.render.TileEntityLightOrbControllerRenderer;
import net.darmo_creations.naissancee.todo_list.ToDoListManager;
import net.darmo_creations.naissancee.world.CustomWorldProviderSurface;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * This mod adds blocks, items and entities that mimic some features from the game NaissanceE.
 *
 * @author Damien Vergnet (Darmo)
 */
@Mod(modid = NaissanceE.MODID, name = NaissanceE.NAME, version = NaissanceE.VERSION)
public class NaissanceE {
  public static final String MODID = "naissancee";
  public static final String NAME = "NaissanceE";
  public static final String VERSION = "1.0";

  public static SimpleNetworkWrapper network;

  public static CalculatorsManager CALCULATORS_MANAGER;
  public static ToDoListManager TODO_LISTS_MANAGER;

  /**
   * This mod???s creative tab.
   */
  public static final CreativeTabs CREATIVE_TAB = new NaissanceETab();

  /**
   * This mod???s logger.
   */
  public static Logger logger;

  // Global entities ID.
  private int entitiesID = 0;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();

    network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    network.registerMessage(PacketLightOrbControllerData.Handler.class, PacketLightOrbControllerData.class, 0, Side.SERVER);
    network.registerMessage(PacketLaserTelemeterData.Handler.class, PacketLaserTelemeterData.class, 1, Side.SERVER);

    for (EntityEntry e : ModEntities.ENTITIES) {
      EntityRegistry.registerModEntity(e.getRegistryName(), e.getEntityClass(), e.getName(), this.entitiesID++, this, 100, 3, true, 0xffffff, 0xffffff);
    }

    // Replace default world provider class of Overworld dimension by custom one to alter skybox
    try {
      Field field = getDimensionTypeClazzField();
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
      field.set(DimensionType.OVERWORLD, CustomWorldProviderSurface.class);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }

    if (event.getSide() == Side.CLIENT) {
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInvisibleLightSource.class, new TileEntityInvisibleLightSourceRenderer());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLightOrbController.class, new TileEntityLightOrbControllerRenderer());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserTelemeter.class, new TileEntityLaserTelemeterRenderer());
      RenderingRegistry.registerEntityRenderingHandler(EntityLightOrb.class, RenderLightOrb::new); // Does not actually render anything
      RenderingRegistry.registerEntityRenderingHandler(EntityPlayerPusher.class, RenderPlayerPusher::new); // Does not actually render anything
      MinecraftForge.EVENT_BUS.register(new GuiToDoList(Minecraft.getMinecraft()));
    }
  }

  /**
   * Return the clazz field of {@link DimensionType} enum.
   * <p>
   * Works for de-obfuscated and some re-obfuscated versions.
   */
  private static Field getDimensionTypeClazzField() {
    String[] names = {
        "field_186077_g", // Obfuscated Forge 1.12.2-14.23.5.2854, mappings 20171003-1.12
        "clazz", // De-obfuscatted
    };
    for (String name : names) {
      try {
        Field field = DimensionType.class.getDeclaredField(name);
        field.setAccessible(true);
        return field;
      } catch (NoSuchFieldException ignored) {
      }
    }
    throw new RuntimeException("DimensionType.clazz field not found!");
  }

  @Mod.EventHandler
  public void serverStarting(FMLServerStartingEvent event) {
    event.registerServerCommand(new CalculatorCommand());
    event.registerServerCommand(new ToDoListCommand());
  }

  @Mod.EventBusSubscriber
  static class EventsHandler {
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
      World world = event.getWorld();
      if (CALCULATORS_MANAGER == null) { // Prevent reloading for each dimension (Nether, End, etc.)
        CALCULATORS_MANAGER = CalculatorsManager.attachToGlobalStorage(world);
      }
      if (TODO_LISTS_MANAGER == null) { // Same as above
        TODO_LISTS_MANAGER = ToDoListManager.attachToGlobalStorage(world);
      }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
      event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
      GameRegistry.registerTileEntity(TileEntityInvisibleLightSource.class, new ResourceLocation(MODID, "invisible_light_source"));
      GameRegistry.registerTileEntity(TileEntityLightOrbController.class, new ResourceLocation(MODID, "light_orb_controller"));
      GameRegistry.registerTileEntity(TileEntityFloatingVariableLightBlock.class, new ResourceLocation(MODID, "floating_variable_light_block"));
      GameRegistry.registerTileEntity(TileEntityLaserTelemeter.class, new ResourceLocation(MODID, "laser_telemeter"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
      event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
      // ItemBlocks
      event.getRegistry().registerAll(ModBlocks.BLOCKS.stream()
          .filter(block -> !(block instanceof IModBlock) || ((IModBlock) block).hasGeneratedItemBlock())
          .map(block -> {
            ItemBlock itemBlock = new ItemBlock(block);
            //noinspection ConstantConditions
            itemBlock.setRegistryName(block.getRegistryName());
            ModItems.ITEM_BLOCKS.put(block, itemBlock);
            return itemBlock;
          })
          .toArray(Item[]::new)
      );
    }
  }

  @Mod.EventBusSubscriber(value = Side.CLIENT)
  public static class ModelRegistrationHandler {
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
      ModItems.ITEMS.forEach(item -> {
        if (!item.getHasSubtypes()) {
          //noinspection ConstantConditions
          ModelLoader.setCustomModelResourceLocation(item, 0,
              new ModelResourceLocation(item.getRegistryName(), "inventory"));
        } else {
          NonNullList<ItemStack> list = NonNullList.create();
          item.getSubItems(CreativeTabs.SEARCH, list);
          for (ItemStack stack : list) {
            String variantName = stack.getUnlocalizedName().substring(stack.getUnlocalizedName().lastIndexOf('.') + 1);
            ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(),
                new ModelResourceLocation(item.getRegistryName() + "." + variantName, "inventory"));
          }
        }
      });
      //noinspection ConstantConditions
      ModItems.ITEM_BLOCKS.values().forEach(item -> ModelLoader.setCustomModelResourceLocation(item, 0,
          new ModelResourceLocation(item.getRegistryName(), "inventory")));
    }
  }
}

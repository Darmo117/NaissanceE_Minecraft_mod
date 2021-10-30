package net.darmo_creations.naissancee;

import net.darmo_creations.naissancee.blocks.IModBlock;
import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.darmo_creations.naissancee.entities.EntityLightOrb;
import net.darmo_creations.naissancee.entities.ModEntities;
import net.darmo_creations.naissancee.entities.render.RenderLightOrb;
import net.darmo_creations.naissancee.gui.NaissanceETab;
import net.darmo_creations.naissancee.items.ModItems;
import net.darmo_creations.naissancee.network.PacketLightOrbControllerData;
import net.darmo_creations.naissancee.tile_entities.TileEntityInvisibleLightSource;
import net.darmo_creations.naissancee.tile_entities.TileEntityInvisibleLightSourceRenderer;
import net.darmo_creations.naissancee.tile_entities.TileEntityLightOrbController;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

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

  /**
   * This mod’s creative tab.
   */
  public static final CreativeTabs CREATIVE_TAB = new NaissanceETab();

  /**
   * This mod’s logger.
   */
  public static Logger logger;

  // Global entities ID.
  private int entitiesID = 0;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();

    network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    network.registerMessage(PacketLightOrbControllerData.Handler.class, PacketLightOrbControllerData.class, 0, Side.SERVER);

    for (EntityEntry e : ModEntities.ENTITIES) {
      EntityRegistry.registerModEntity(e.getRegistryName(), e.getEntityClass(), e.getName(), this.entitiesID++, this, 100, 3, true, 0xffffff, 0xffffff);
    }

    if (event.getSide() == Side.CLIENT) {
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInvisibleLightSource.class, new TileEntityInvisibleLightSourceRenderer());
      RenderingRegistry.registerEntityRenderingHandler(EntityLightOrb.class, RenderLightOrb::new); // Does not actually render anything
    }
  }

  @Mod.EventBusSubscriber
  static class EventsHandler {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
      event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
      GameRegistry.registerTileEntity(TileEntityInvisibleLightSource.class, new ResourceLocation(MODID, "invisible_light_source"));
      GameRegistry.registerTileEntity(TileEntityLightOrbController.class, new ResourceLocation(MODID, "light_orb_controller"));
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
          for (int i = 0; i < 16; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i,
                new ModelResourceLocation(item.getRegistryName() + "_" + i, "inventory"));
          }
        }
      });
      //noinspection ConstantConditions
      ModItems.ITEM_BLOCKS.values().forEach(item -> ModelLoader.setCustomModelResourceLocation(item, 0,
          new ModelResourceLocation(item.getRegistryName(), "inventory")));
    }
  }
}

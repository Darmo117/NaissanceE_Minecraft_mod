package net.darmo_creations.naissancee.tile_entities.render;

import net.darmo_creations.naissancee.blocks.BlockInvisibleLightSource;
import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.darmo_creations.naissancee.items.ModItems;
import net.darmo_creations.naissancee.tile_entities.TileEntityInvisibleLightSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Renderer for the tile entity associated to invisible light sources.
 * <p>
 * Renders a rotating item that indicates the mode for the tile entity and the block’s light level value.
 *
 * @see TileEntityInvisibleLightSource
 * @see BlockInvisibleLightSource
 * @see ModBlocks#INVISIBLE_LIGHT_SOURCE
 */
@SideOnly(Side.CLIENT)
public class TileEntityInvisibleLightSourceRenderer extends TileEntitySpecialRenderer<TileEntityInvisibleLightSource> {
  @Override
  public void render(TileEntityInvisibleLightSource te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    EntityPlayer player = Minecraft.getMinecraft().player;

    if (player.getHeldItemMainhand().getItem() == ModItems.INVISIBLE_LIGHT_SOURCE_EDITING_TOOL
        || player.getHeldItemOffhand().getItem() == ModItems.INVISIBLE_LIGHT_SOURCE_EDITING_TOOL
        || player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(ModBlocks.INVISIBLE_LIGHT_SOURCE)
        || player.getHeldItemOffhand().getItem() == Item.getItemFromBlock(ModBlocks.INVISIBLE_LIGHT_SOURCE)) {
      GlStateManager.enableRescaleNormal();

      GlStateManager.pushMatrix();
      GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
      GlStateManager.enableBlend();
      RenderHelper.enableStandardItemLighting();
      GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
      GlStateManager.translate(x + 0.5, y + 0.4, z + 0.5);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTicks) * 4, 0, 1, 0);
      this.renderItem(te);
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();

      GlStateManager.pushMatrix();
      GlStateManager.translate(x + 0.75, y + 0.75, z + 0.25);
      GlStateManager.rotate(180, 0, 0, 1);
      float scale = 0.01f;
      float inverseScale = 1 / scale;
      GlStateManager.scale(scale, scale, scale);
      for (int i = 0; i < 4; i++) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(i * 90, 0, 1, 0);
        if (i == 1) {
          GlStateManager.translate(inverseScale * -0.5, 0.0, 0);
        } else if (i == 2) {
          GlStateManager.translate(inverseScale * -0.5, 0.0, inverseScale * -0.5);
        } else if (i == 3) {
          GlStateManager.translate(0.0, 0, inverseScale * -0.5);
        }
        this.renderLightValueText(te);
        GlStateManager.popMatrix();
      }
      GlStateManager.popMatrix();

      GlStateManager.disableRescaleNormal();
    }
  }

  /**
   * Render an item depending on the given tile entity’s mode.
   */
  private void renderItem(TileEntityInvisibleLightSource te) {
    Item item = null;
    switch (te.getMode()) {
      case EDIT:
        item = Item.getItemFromBlock(ModBlocks.INVISIBLE_LIGHT_SOURCE);
        break;
      case LOCKED:
        item = ModItems.INVISIBLE_LIGHT_SOURCE_LOCKED;
        break;
      case REDSTONE:
        item = ModItems.INVISIBLE_LIGHT_SOURCE_REDSTONE;
        break;
    }

    if (item != null) {
      ItemStack stack = new ItemStack(item);
      IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, te.getWorld(), null);
      model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

      Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);
    }
  }

  /**
   * Render light level value using the given tile entity.
   */
  private void renderLightValueText(TileEntityInvisibleLightSource te) {
    FontRenderer fontrenderer = this.getFontRenderer();
    String s = "" + te.getWorld().getBlockState(te.getPos()).getValue(BlockInvisibleLightSource.LIGHT_LEVEL);
    fontrenderer.drawString(s, 0, 0, 0);
  }
}

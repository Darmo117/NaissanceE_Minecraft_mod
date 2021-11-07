package net.darmo_creations.naissancee.tile_entities.render;

import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.darmo_creations.naissancee.tile_entities.TileEntityLaserTelemeter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Renderer for the tile entity associated to laser telemeters.
 * <p>
 * Renders the axes/box.
 *
 * @see TileEntityLaserTelemeterRenderer
 * @see net.darmo_creations.naissancee.blocks.BlockLaserTelemeter
 * @see ModBlocks#LASER_TELEMETER
 */
@SideOnly(Side.CLIENT)
public class TileEntityLaserTelemeterRenderer extends TileEntitySpecialRenderer<TileEntityLaserTelemeter> {
  @Override
  public void render(TileEntityLaserTelemeter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    EntityPlayer player = Minecraft.getMinecraft().player;

    if (player.canUseCommandBlock() || player.isSpectator()) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      GlStateManager.disableFog();
      GlStateManager.disableLighting();
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      this.setLightmapDisabled(true);

      this.renderBox(te, x, y, z, tessellator, bufferbuilder);

      this.setLightmapDisabled(false);
      GlStateManager.enableLighting();
      GlStateManager.enableTexture2D();
      GlStateManager.enableDepth();
      GlStateManager.depthMask(true);
      GlStateManager.enableFog();
    }
  }

  private void renderBox(TileEntityLaserTelemeter te, double x, double y, double z,
                         Tessellator tessellator, BufferBuilder bufferBuilder) {
    Vec3i size = te.getSize();
    BlockPos offset = te.getOffset();
    int xOff = offset.getX();
    int yOff = offset.getY();
    int zOff = offset.getZ();
    double boxX1 = x + xOff;
    double boxY1 = y + yOff;
    double boxZ1 = z + zOff;
    double boxX2 = x + size.getX() + xOff;
    double boxY2 = y + size.getY() + yOff;
    double boxZ2 = z + size.getZ() + zOff;

    GlStateManager.glLineWidth(2);
    bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    RenderGlobal.drawBoundingBox(bufferBuilder, boxX1, boxY1, boxZ1, boxX2, boxY2, boxZ2, 1, 1, 1, 1);
    tessellator.draw();

    GlStateManager.glLineWidth(3);
    bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    bufferBuilder.pos(boxX1, boxY1, boxZ1).color(255, 0, 0, 255).endVertex();
    bufferBuilder.pos(boxX2, boxY1, boxZ1).color(255, 0, 0, 255).endVertex();
    tessellator.draw();
    bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    bufferBuilder.pos(boxX1, boxY1, boxZ1).color(0, 255, 0, 255).endVertex();
    bufferBuilder.pos(boxX1, boxY2, boxZ1).color(0, 255, 0, 255).endVertex();
    tessellator.draw();
    bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    bufferBuilder.pos(boxX1, boxY1, boxZ1).color(0, 0, 255, 255).endVertex();
    bufferBuilder.pos(boxX1, boxY1, boxZ2).color(0, 0, 255, 255).endVertex();
    tessellator.draw();

    GlStateManager.glLineWidth(1);
  }

  // Beware, does not seem to work when running through IDE
  @Override
  public boolean isGlobalRenderer(TileEntityLaserTelemeter te) {
    return true;
  }
}

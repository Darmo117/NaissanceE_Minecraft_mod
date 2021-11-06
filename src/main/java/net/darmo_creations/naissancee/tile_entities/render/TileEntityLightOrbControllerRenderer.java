package net.darmo_creations.naissancee.tile_entities.render;

import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.darmo_creations.naissancee.items.ModItems;
import net.darmo_creations.naissancee.tile_entities.PathCheckpoint;
import net.darmo_creations.naissancee.tile_entities.TileEntityLightOrbController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Renderer for the tile entity associated to light orb controllers.
 * <p>
 * Renders the checkpoints as cubes and the path as straight lines between checkpoints.
 *
 * @see TileEntityLightOrbController
 * @see net.darmo_creations.naissancee.blocks.BlockLightOrbController
 * @see ModBlocks#LIGHT_ORB_CONTROLLER
 */
@SideOnly(Side.CLIENT)
public class TileEntityLightOrbControllerRenderer extends TileEntitySpecialRenderer<TileEntityLightOrbController> {
  @Override
  public void render(TileEntityLightOrbController te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    EntityPlayer player = Minecraft.getMinecraft().player;

    if ((Minecraft.getMinecraft().player.canUseCommandBlock() || Minecraft.getMinecraft().player.isSpectator())
        && player.getHeldItemMainhand().getItem() == ModItems.LIGHT_ORB_TWEAKER
        || player.getHeldItemOffhand().getItem() == ModItems.LIGHT_ORB_TWEAKER) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      GlStateManager.disableFog();
      GlStateManager.disableLighting();
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      this.setLightmapDisabled(true);

      List<PathCheckpoint> checkpoints = te.getCheckpoints();
      for (int i = 0, size = checkpoints.size(); i < size; i++) {
        PathCheckpoint checkpoint = checkpoints.get(i);
        PathCheckpoint nextCheckpoint = null;
        if (i == size - 1) {
          if (te.loops()) {
            nextCheckpoint = checkpoints.get(0);
          }
        } else {
          nextCheckpoint = checkpoints.get(i + 1);
        }
        this.renderCheckpoint(te, x, y, z, checkpoint, tessellator, bufferbuilder);
        if (nextCheckpoint != null) {
          // TODO draw line from current to next checkpoint
        }
      }

      this.setLightmapDisabled(false);
      GlStateManager.glLineWidth(1F);
      GlStateManager.enableLighting();
      GlStateManager.enableTexture2D();
      GlStateManager.enableDepth();
      GlStateManager.depthMask(true);
      GlStateManager.enableFog();
    }
  }

  private void renderCheckpoint(TileEntityLightOrbController te, double x, double y, double z, PathCheckpoint checkpoint, Tessellator tessellator, BufferBuilder bufferBuilder) {
    GlStateManager.glLineWidth(3f);
    bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    BlockPos tePos = te.getPos();
    BlockPos checkpointPos = checkpoint.getPos();

    final double size = 0.25;
    double start = 0.5 - size;
    double end = 0.5 + size;
    double x1 = checkpointPos.getX() - tePos.getX() + start + x;
    double y1 = checkpointPos.getY() - tePos.getY() + start + y;
    double z1 = checkpointPos.getZ() - tePos.getZ() + start + z;
    double x2 = checkpointPos.getX() - tePos.getX() + end + x;
    double y2 = checkpointPos.getY() - tePos.getY() + end + y;
    double z2 = checkpointPos.getZ() - tePos.getZ() + end + z;

    int r = 0, g = 0, b = 0;
    if (checkpoint.isStop()) {
      r = 1;
    } else {
      g = 1;
    }

    RenderGlobal.drawBoundingBox(bufferBuilder, x1, y1, z1, x2, y2, z2, r, g, b, 1);

    tessellator.draw();
  }

  // FIXME ignored
  @Override
  public boolean isGlobalRenderer(TileEntityLightOrbController te) {
    return true;
  }
}

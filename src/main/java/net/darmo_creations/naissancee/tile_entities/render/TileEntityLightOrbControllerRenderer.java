package net.darmo_creations.naissancee.tile_entities.render;

import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.darmo_creations.naissancee.items.ItemLightOrbTweaker;
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
import net.minecraft.item.ItemStack;
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
    ItemStack stack = player.getHeldItemMainhand();

    if ((player.canUseCommandBlock() || player.isSpectator())
        && stack.getItem() == ModItems.LIGHT_ORB_TWEAKER
        && ItemLightOrbTweaker.getControllerTileEntity(stack, this.getWorld()).map(t -> t.getPos().equals(te.getPos())).orElse(false)) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      GlStateManager.disableFog();
      GlStateManager.disableLighting();
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      this.setLightmapDisabled(true);

      this.renderControllerBox(x, y, z, tessellator, bufferbuilder);

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
        this.renderCheckpoint(te, x, y, z, checkpoint, i == 0, i == size - 1, tessellator, bufferbuilder);
        if (nextCheckpoint != null) {
          this.renderLine(te, x, y, z, checkpoint, nextCheckpoint, tessellator, bufferbuilder);
        }
      }

      this.setLightmapDisabled(false);
      GlStateManager.enableLighting();
      GlStateManager.enableTexture2D();
      GlStateManager.enableDepth();
      GlStateManager.depthMask(true);
      GlStateManager.enableFog();
    }
  }

  private void renderControllerBox(double x, double y, double z, Tessellator tessellator, BufferBuilder bufferBuilder) {
    final double offset = 0.01;
    double boxX1 = x - offset;
    double boxY1 = y - offset;
    double boxZ1 = z - offset;
    double boxX2 = x + 1 + offset;
    double boxY2 = y + 1 + offset;
    double boxZ2 = z + 1 + offset;

    GlStateManager.glLineWidth(3);
    bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    RenderGlobal.drawBoundingBox(bufferBuilder, boxX1, boxY1, boxZ1, boxX2, boxY2, boxZ2, 1, 1, 0, 1);
    tessellator.draw();
    GlStateManager.glLineWidth(1);
  }

  private void renderCheckpoint(TileEntityLightOrbController te, double x, double y, double z, PathCheckpoint checkpoint,
                                boolean isFirst, boolean isLast, Tessellator tessellator, BufferBuilder bufferBuilder) {
    BlockPos tePos = te.getPos();
    BlockPos checkpointPos = checkpoint.getPos();

    final double boxSize = 0.25;
    double start = 0.5 - boxSize;
    double end = 0.5 + boxSize;

    double boxX1 = checkpointPos.getX() - tePos.getX() + start + x;
    double boxY1 = checkpointPos.getY() - tePos.getY() + start + y;
    double boxZ1 = checkpointPos.getZ() - tePos.getZ() + start + z;
    double boxX2 = checkpointPos.getX() - tePos.getX() + end + x;
    double boxY2 = checkpointPos.getY() - tePos.getY() + end + y;
    double boxZ2 = checkpointPos.getZ() - tePos.getZ() + end + z;

    double lineX = checkpointPos.getX() - tePos.getX() + 0.5 + x;
    double lineY1 = checkpointPos.getY() - tePos.getY() + y;
    double lineZ = checkpointPos.getZ() - tePos.getZ() + 0.5 + z;
    double lineY2 = checkpointPos.getY() - tePos.getY() + 1 + y;

    int boxR = 0, boxG = 0, boxB = 0;
    if (checkpoint.isStop()) {
      boxR = 1;
    } else {
      boxG = 1;
    }
    int lineR = 0, lineG = 0, lineB = 0;
    if (isFirst) {
      lineB = 255;
    } else if (isLast) {
      lineG = 255;
      lineB = 255;
    }

    GlStateManager.glLineWidth(3);
    bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    if (isFirst || isLast) {
      bufferBuilder.pos(lineX, lineY1, lineZ).color(lineR, lineG, lineB, 255).endVertex();
      bufferBuilder.pos(lineX, lineY2, lineZ).color(lineR, lineG, lineB, 255).endVertex();
    }
    RenderGlobal.drawBoundingBox(bufferBuilder, boxX1, boxY1, boxZ1, boxX2, boxY2, boxZ2, boxR, boxG, boxB, 1);
    tessellator.draw();
    GlStateManager.glLineWidth(1);
  }

  private void renderLine(TileEntityLightOrbController te, double x, double y, double z,
                          PathCheckpoint checkpoint1, PathCheckpoint checkpoint2,
                          Tessellator tessellator, BufferBuilder bufferBuilder) {
    BlockPos tePos = te.getPos();
    BlockPos pos1 = checkpoint1.getPos();
    BlockPos pos2 = checkpoint2.getPos();
    final double offset = 0.5;
    double x1 = pos1.getX() - tePos.getX() + x + offset;
    double y1 = pos1.getY() - tePos.getY() + y + offset;
    double z1 = pos1.getZ() - tePos.getZ() + z + offset;
    double x2 = pos2.getX() - tePos.getX() + x + offset;
    double y2 = pos2.getY() - tePos.getY() + y + offset;
    double z2 = pos2.getZ() - tePos.getZ() + z + offset;

    GlStateManager.glLineWidth(2);
    bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    bufferBuilder.pos(x1, y1, z1).color(255, 255, 255, 255).endVertex();
    bufferBuilder.pos(x2, y2, z2).color(255, 255, 255, 255).endVertex();
    tessellator.draw();
    GlStateManager.glLineWidth(1);
  }

  // Beware, does not seem to work when running through IDE
  @Override
  public boolean isGlobalRenderer(TileEntityLightOrbController te) {
    return true;
  }
}

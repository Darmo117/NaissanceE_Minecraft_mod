package net.darmo_creations.naissancee.entities.render;

import net.darmo_creations.naissancee.entities.EntityPlayerPusher;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Renders nothing as player pushers are invisible.
 */
public class RenderPlayerPusher extends Render<EntityPlayerPusher> {
  public RenderPlayerPusher(RenderManager renderManager) {
    super(renderManager);
  }

  @Override
  protected ResourceLocation getEntityTexture(EntityPlayerPusher entity) {
    return null; // No texture as pushers are invisible
  }
}

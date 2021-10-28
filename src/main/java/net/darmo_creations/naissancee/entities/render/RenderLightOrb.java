package net.darmo_creations.naissancee.entities.render;

import net.darmo_creations.naissancee.entities.EntityLightOrb;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Renders nothing as light orbs generate particles instead of having a model.
 */
public class RenderLightOrb extends Render<EntityLightOrb> {
  public RenderLightOrb(RenderManager renderManager) {
    super(renderManager);
  }

  @Override
  protected ResourceLocation getEntityTexture(EntityLightOrb entity) {
    return null; // No texture as orbs use particles
  }
}

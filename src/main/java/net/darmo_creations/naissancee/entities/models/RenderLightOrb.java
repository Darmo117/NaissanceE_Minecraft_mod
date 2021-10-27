package net.darmo_creations.naissancee.entities.models;

import net.darmo_creations.naissancee.entities.EntityLightOrb;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Renders nothing as light orb generates particles instead of having a model.
 */
public class RenderLightOrb extends Render<EntityLightOrb> {
  public RenderLightOrb(RenderManager renderManager) {
    super(renderManager);
  }

  @Override
  protected ResourceLocation getEntityTexture(EntityLightOrb entity) {
    return null;
  }
}

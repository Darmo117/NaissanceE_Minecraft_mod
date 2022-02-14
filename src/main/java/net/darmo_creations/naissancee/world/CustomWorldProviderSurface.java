package net.darmo_creations.naissancee.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.client.IRenderHandler;

/**
 * Custom world provider for the Overworld dimension that has no rendered skybox and no sunlight.
 */
public class CustomWorldProviderSurface extends WorldProviderSurface {
  @Override
  protected void init() {
    super.init();
    this.hasSkyLight = false;
    if (this.world.isRemote) {
      this.setSkyRenderer(new IRenderHandler() {
        @Override
        public void render(float partialTicks, WorldClient world, Minecraft mc) {
          // Does not render anything
        }
      });
    }
  }

  @Override
  protected void generateLightBrightnessTable() {
    for (int i = 0; i <= 15; ++i) {
      float f1 = 1F - (float) i / 15F;
      this.lightBrightnessTable[i] = (1F - f1) / (f1 * 3F + 1F) * 0.99F + 0.01F;
    }
  }

  @Override
  public boolean isSkyColored() {
    return false;
  }

  @Override
  public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
    return new Vec3d(0, 0, 0);
  }
}

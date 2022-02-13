package net.darmo_creations.naissancee.entities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

/**
 * This type of entity is used to push the player to simulate wind.
 * <p>
 * Extends {@link EntityLiving} to enable collision.
 */
public class EntityPlayerPusher extends EntityLiving {
  public EntityPlayerPusher(World world) {
    super(world);
    this.setSize(1F, 1F);
  }
}

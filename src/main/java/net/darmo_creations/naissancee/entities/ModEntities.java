package net.darmo_creations.naissancee.entities;

import net.darmo_creations.naissancee.NaissanceE;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class declares all entities for this mod.
 */
@SuppressWarnings("unused")
public final class ModEntities {
  public static final EntityEntry MOVING_LIGHT = new EntityEntry(EntityLightOrb.class, NaissanceE.MODID + ".light_orb").setRegistryName("light_orb");

  /**
   * The list of all declared entities for this mod.
   */
  public static final List<EntityEntry> ENTITIES = new LinkedList<>();

  static {
    Arrays.stream(ModEntities.class.getDeclaredFields())
        .filter(field -> EntityEntry.class.isAssignableFrom(field.getType()))
        .map(field -> {
          try {
            return (EntityEntry) field.get(null);
          } catch (IllegalAccessException e) {
            // Should never happen
            throw new RuntimeException(e);
          }
        })
        .forEach(ENTITIES::add);
  }

  private ModEntities() {
  }
}

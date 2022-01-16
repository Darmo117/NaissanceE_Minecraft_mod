package net.darmo_creations.naissancee.calculator;

import net.darmo_creations.naissancee.DataManager;
import net.darmo_creations.naissancee.NaissanceE;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;

/**
 * Manager for global and per-player {@link Calculator} instances.
 */
public class CalculatorsManager extends DataManager<Calculator> {
  public static final String DATA_NAME = NaissanceE.MODID + "calculator";

  /**
   * Required by Forge, do not use.
   */
  public CalculatorsManager() {
    this(DATA_NAME);
  }

  /**
   * Required by Forge, do not use.
   */
  public CalculatorsManager(String name) {
    super(name);
  }

  @Override
  protected Calculator getDefaultDataValue() {
    return new Calculator();
  }

  /**
   * Attaches a manager to the global storage through a world instance.
   * If no manager instance is already defined, a new one is created and attached to the storage.
   *
   * @param world The world used to access the global storage.
   * @return The manager instance.
   */
  public static CalculatorsManager attachToGlobalStorage(World world) {
    MapStorage storage = world.getMapStorage();
    //noinspection ConstantConditions
    CalculatorsManager instance = (CalculatorsManager) storage.getOrLoadData(CalculatorsManager.class, DATA_NAME);

    if (instance == null) {
      instance = new CalculatorsManager();
      storage.setData(DATA_NAME, instance);
    }
    return instance;
  }
}

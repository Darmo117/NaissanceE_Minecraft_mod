package net.darmo_creations.naissancee.calculator;

import net.darmo_creations.naissancee.DataManager;
import net.darmo_creations.naissancee.NaissanceE;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CalculatorsManager extends DataManager<Calculator> {
  public static final String DATA_NAME = NaissanceE.MODID + "calculator";

  public CalculatorsManager() {
    this(DATA_NAME);
  }

  public CalculatorsManager(String name) {
    super(name);
  }

  @Override
  protected Calculator getDefaultDataValue() {
    return new Calculator();
  }

  public static CalculatorsManager attachToWorld(World world) {
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

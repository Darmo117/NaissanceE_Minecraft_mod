package net.darmo_creations.naissancee.todo_list;

import net.darmo_creations.naissancee.DataManager;
import net.darmo_creations.naissancee.NaissanceE;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;

/**
 * Manager for global and per-player {@link ToDoList} instances.
 */
public class ToDoListManager extends DataManager<ToDoList> {
  public static final String DATA_NAME = NaissanceE.MODID + "todo_list";

  /**
   * Required by Forge, do not use.
   */
  public ToDoListManager() {
    this(DATA_NAME);
  }

  /**
   * Required by Forge, do not use.
   */
  public ToDoListManager(String name) {
    super(name);
  }

  @Override
  protected ToDoList getDefaultDataValue() {
    return new ToDoList();
  }

  /**
   * Attaches a manager to the global storage through a world instance.
   * If no manager instance is already defined, a new one is created and attached to the storage.
   *
   * @param world The world used to access the global storage.
   * @return The manager instance.
   */
  public static ToDoListManager attachToGlobalStorage(World world) {
    MapStorage storage = world.getMapStorage();
    //noinspection ConstantConditions
    ToDoListManager instance = (ToDoListManager) storage.getOrLoadData(ToDoListManager.class, DATA_NAME);

    if (instance == null) {
      instance = new ToDoListManager();
      storage.setData(DATA_NAME, instance);
    }
    return instance;
  }
}

package net.darmo_creations.naissancee;

public interface ManagedData<T extends ManagedData<T>> extends NBTSerializable {
  void setManager(DataManager<T> manager);
}

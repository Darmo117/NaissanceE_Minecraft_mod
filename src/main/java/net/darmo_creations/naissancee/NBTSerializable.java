package net.darmo_creations.naissancee;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Enables serialization and deserialization of objects into and from NBT tags.
 */
public interface NBTSerializable {
  /**
   * Serialize this object into an NBT tag.
   *
   * @return The serialized data.
   */
  NBTTagCompound writeToNBT();

  /**
   * Update this object using data in the given tag.
   *
   * @param tag The data to deserialize.
   */
  void readFromNBT(NBTTagCompound tag);
}

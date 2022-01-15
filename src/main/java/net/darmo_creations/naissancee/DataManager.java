package net.darmo_creations.naissancee;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A {@link DataManager} manages a global and per-player data objects.
 * These objects are saved on the server alongside world data.
 *
 * @param <T> Type of managed data.
 */
public abstract class DataManager<T extends ManagedData<T>> extends WorldSavedData {
  private static final String GLOBAL_DATA_KEY = "GlobalData";
  private static final String PLAYERS_DATA_KEY = "PlayersData";
  private static final String UUID_KEY = "UUID";
  private static final String PLAYER_DATA_KEY = "PlayerData";

  private T globalData;
  private final Map<UUID, T> playerData;

  /**
   * Create a manager with the given name.
   *
   * @param name Manager’s name.
   */
  public DataManager(String name) {
    super(name);
    this.globalData = this.getDefaultDataValue();
    this.globalData.setManager(this);
    this.playerData = new HashMap<>();
  }

  /**
   * Return a default data object.
   */
  protected abstract T getDefaultDataValue();

  /**
   * Return the global data object.
   */
  public T getGlobalData() {
    return this.globalData;
  }

  /**
   * Return the data object associated to the given data object.
   * If no data object for the player exists, a new one is created then returned.
   *
   * @param player The player.
   * @return The associated data object.
   */
  public T getOrCreatePlayerData(final EntityPlayer player) {
    UUID playerUUID = player.getGameProfile().getId();
    if (!this.playerData.containsKey(playerUUID)) {
      T data = this.getDefaultDataValue();
      data.setManager(this);
      this.playerData.put(playerUUID, data);
      this.markDirty();
    }
    return this.playerData.get(playerUUID);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    tag.setTag(GLOBAL_DATA_KEY, this.globalData.writeToNBT());
    NBTTagList list = new NBTTagList();
    for (Map.Entry<UUID, T> item : this.playerData.entrySet()) {
      NBTTagCompound itemTag = new NBTTagCompound();
      itemTag.setTag(UUID_KEY, NBTUtil.createUUIDTag(item.getKey()));
      itemTag.setTag(PLAYER_DATA_KEY, item.getValue().writeToNBT());
      list.appendTag(itemTag);
    }
    tag.setTag(PLAYERS_DATA_KEY, list);
    return tag;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    this.globalData = this.getDefaultDataValue();
    this.globalData.readFromNBT(tag.getCompoundTag(GLOBAL_DATA_KEY));
    this.globalData.setManager(this);
    this.playerData.clear();
    for (NBTBase item : tag.getTagList(PLAYERS_DATA_KEY, new NBTTagCompound().getId())) {
      NBTTagCompound c = (NBTTagCompound) item;
      T playerData = this.getDefaultDataValue();
      playerData.readFromNBT(c.getCompoundTag(PLAYER_DATA_KEY));
      playerData.setManager(this);
      this.playerData.put(c.getUniqueId(UUID_KEY), playerData);
    }
  }
}

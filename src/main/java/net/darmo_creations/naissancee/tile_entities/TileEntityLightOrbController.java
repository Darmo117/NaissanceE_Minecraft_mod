package net.darmo_creations.naissancee.tile_entities;

import net.darmo_creations.naissancee.blocks.BlockLightOrbController;
import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.darmo_creations.naissancee.entities.EntityLightOrb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Tile entity for light orb controller.
 * <p>
 * Creates a light orb entity when the tile entity is created, specifically when {@link #init()} is called.
 * It defines the behavior its associated light orb.
 *
 * @see BlockLightOrbController
 * @see ModBlocks#LIGHT_ORB_CONTROLLER
 */
public class TileEntityLightOrbController extends TileEntity {
  private static final String ORB_ID_TAG_KEY = "OrbID";
  private static final String ACTIVE_TAG_KEY = "Active";
  private static final String CHECKPOINTS_TAG_KEY = "Checkpoints";
  private static final String LOOPS_TAG_KEY = "Loops";
  private static final String LIGHT_LEVEL_TAG_KEY = "LightLevel";
  private static final String SPEED_TAG_KEY = "Speed";

  /**
   * UUID of associated orb.
   */
  private UUID orbID;
  /**
   * Whether the orb is active, i.e. whether the player can interact with it.
   */
  private boolean active;
  /**
   * Path the light orb must follow.
   */
  private List<PathCheckpoint> checkpoints;
  /**
   * Whether the path loops, i.e. whether the orb should go back
   * to start checkpoint after it has reached the last one.
   */
  private boolean loops;
  /**
   * Light level of the light blocks placed by the entity.
   */
  private int lightLevel;
  /**
   * Movement speed of orb in blocks per second.
   */
  private double speed;

  /**
   * Create a tile entity with empty path.
   */
  public TileEntityLightOrbController() {
    this.checkpoints = new LinkedList<>();
  }

  /**
   * Initialize this tile entity.
   * The path is initialized with a single stop checkpoint positionned right above the controller block.
   * Light level is set to 15 and speed to 0.25 blocks per second. Path does not loop by default.
   */
  public void init() {
    this.setActive(false);
    this.setLightLevel(15);
    this.setLoops(false);
    this.setSpeed(0.25);
    this.checkpoints = new LinkedList<>();
    this.addCheckpoint(this.pos.up(), true);
    this.spawnOrb();
  }

  /**
   * Spawns a new light orb after deleting the already existing one if it exists.
   * Does nothing when called client-side.
   */
  public void spawnOrb() {
    if (!this.world.isRemote) {
      EntityLightOrb currentOrb = this.getOrb();
      if (currentOrb != null) {
        this.world.removeEntity(currentOrb);
      }
      EntityLightOrb newOrb = new EntityLightOrb(this.world, this);
      BlockPos pos = this.checkpoints.get(0).getPos();
      newOrb.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
      this.world.spawnEntity(newOrb);
      this.orbID = newOrb.getUniqueID();
      newOrb.init();
      this.markDirty();
    }
  }

  /**
   * Kills the associated light orb.
   * Does nothing when called client-side.
   */
  public void killOrb() {
    if (!this.world.isRemote) {
      EntityLightOrb currentOrb = this.getOrb();
      if (currentOrb != null) {
        this.world.removeEntity(currentOrb);
        this.markDirty();
      }
    }
  }

  /**
   * Get the associated orb entity, based on its UUID.
   *
   * @return The entity object or null if it doesn’t exist.
   * @see #orbID
   */
  private EntityLightOrb getOrb() {
    List<EntityLightOrb> orbs = this.world.getEntities(EntityLightOrb.class, e -> e.getUniqueID().equals(this.orbID));
    return !orbs.isEmpty() ? orbs.get(0) : null;
  }

  /**
   * Whether this controller is active, i.e. the light orb should react to player.
   */
  public boolean isActive() {
    return this.active;
  }

  /**
   * Set active state.
   *
   * @param active Whether the orb should interact with the player.
   */
  public void setActive(boolean active) {
    this.active = active;
    this.markDirty();
  }

  /**
   * Adds a new checkpoint for the given position at the end of the path.
   *
   * @param pos  Checkpoint’s position.
   * @param stop Whether the orb should stop at this checkpoint.
   */
  public void addCheckpoint(BlockPos pos, boolean stop) {
    this.checkpoints.add(new PathCheckpoint(pos, stop));
    this.spawnOrb();
  }

  /**
   * Remove all checkpoints at the given position.
   *
   * @param pos The position to delete checkpoints from.
   * @return The number of checkpoints that were removed.
   */
  public int removeCheckpoint(BlockPos pos) {
    int oldSize = this.checkpoints.size();
    List<PathCheckpoint> checkpoints = new LinkedList<>(this.checkpoints);
    boolean anyRemoved = checkpoints.removeIf(pc -> pc.getPos().equals(pos));
    if (checkpoints.isEmpty()) { // If list becomes empty, abort
      return 0;
    }
    this.checkpoints = checkpoints;
    if (anyRemoved) {
      this.spawnOrb();
    }
    return oldSize - this.checkpoints.size();
  }

  /**
   * Get the list of checkpoints.
   */
  public List<PathCheckpoint> getCheckpoints() {
    return this.checkpoints.stream().map(PathCheckpoint::clone).collect(Collectors.toList());
  }

  /**
   * Set the list of checkpoints. Should only be used for syncing.
   *
   * @param checkpoints The list of checkpoints.
   * @throws IllegalArgumentException If the list is empty.
   * @see net.darmo_creations.naissancee.network.PacketLightOrbControllerData
   */
  public void setCheckpoints(List<PathCheckpoint> checkpoints) {
    if (checkpoints.size() == 0) {
      throw new IllegalArgumentException("checkpoints list is empty");
    }
    this.checkpoints = checkpoints.stream().map(PathCheckpoint::clone).collect(Collectors.toCollection(LinkedList::new));
    this.markDirty();
  }

  /**
   * Whether the path has a checkpoint at the given position.
   *
   * @param pos A block position.
   * @return True if there is at least one checkpoint at this position, false if there are none.
   */
  public boolean hasCheckpointAt(BlockPos pos) {
    return this.checkpoints.stream().anyMatch(pc -> pc.getPos().equals(pos));
  }

  /**
   * Return the checkpoint that immediatly follows the one at the given index.
   *
   * @param index A checkpoint index.
   * @return The next checkpoint with its index; an empty value if the checkpoint
   * at the given index is the last one and the path does not loop.
   */
  public Optional<Pair<Integer, PathCheckpoint>> getNextCheckpoint(int index) {
    if (index < this.checkpoints.size() - 1) {
      return Optional.of(new ImmutablePair<>(index + 1, this.checkpoints.get(index + 1).clone()));
    } else if (this.loops) {
      return Optional.of(new ImmutablePair<>(0, this.checkpoints.get(0).clone()));
    }
    return Optional.empty();
  }

  /**
   * Whether the path loops.
   */
  public boolean loops() {
    return this.loops;
  }

  public void setLoops(boolean loops) {
    this.loops = loops;
    this.markDirty();
  }

  /**
   * Get the light orb’s light level.
   */
  public int getLightLevel() {
    return this.lightLevel;
  }

  /**
   * Sets the orb’s light level.
   *
   * @param lightLevel The new light level.
   * @throws IllegalArgumentException If light level is outside [0, 15] range.
   */
  public void setLightLevel(int lightLevel) {
    if (lightLevel < 0 || lightLevel > 15) {
      throw new IllegalArgumentException("invalid light value " + lightLevel);
    }
    this.lightLevel = lightLevel;
    this.markDirty();
  }

  /**
   * Get light orb’s speed (blocks per second).
   */
  public double getSpeed() {
    return this.speed;
  }

  /**
   * Set light orb’s speed (blocks per second).
   *
   * @param speed The new speed.
   * @throws IllegalArgumentException If the speed is negative.
   */
  public void setSpeed(double speed) {
    if (speed < 0) {
      throw new IllegalArgumentException("negative speed");
    }
    this.speed = speed;
    this.markDirty();
  }

  // Prevent TE from being reset on block state change.
  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    // Check if block is still correct to prevent bugs
    return !(newSate.getBlock() instanceof BlockLightOrbController);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setTag(ORB_ID_TAG_KEY, NBTUtil.createUUIDTag(this.orbID));
    compound.setBoolean(ACTIVE_TAG_KEY, this.active);
    compound.setBoolean(LOOPS_TAG_KEY, this.loops);
    compound.setInteger(LIGHT_LEVEL_TAG_KEY, this.lightLevel);
    compound.setDouble(SPEED_TAG_KEY, this.speed);
    NBTTagList list = new NBTTagList();
    for (PathCheckpoint checkpoint : this.checkpoints) {
      list.appendTag(checkpoint.toNBT());
    }
    compound.setTag(CHECKPOINTS_TAG_KEY, list);
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.orbID = NBTUtil.getUUIDFromTag(compound.getCompoundTag(ORB_ID_TAG_KEY));
    this.active = compound.getBoolean(ACTIVE_TAG_KEY);
    this.loops = compound.getBoolean(LOOPS_TAG_KEY);
    this.lightLevel = compound.getInteger(LIGHT_LEVEL_TAG_KEY);
    this.speed = compound.getDouble(SPEED_TAG_KEY);
    this.checkpoints = new LinkedList<>();
    for (NBTBase tag : compound.getTagList(CHECKPOINTS_TAG_KEY, new NBTTagCompound().getId())) {
      this.checkpoints.add(new PathCheckpoint((NBTTagCompound) tag));
    }
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    this.readFromNBT(pkt.getNbtCompound());
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    return this.writeToNBT(new NBTTagCompound());
  }

}

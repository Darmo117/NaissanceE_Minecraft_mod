package net.darmo_creations.naissancee.tile_entities;

import net.darmo_creations.naissancee.blocks.BlockLightOrbController;
import net.darmo_creations.naissancee.entities.EntityLightOrb;
import net.darmo_creations.naissancee.entities.IPathCheckpoint;
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

import java.util.*;

public class TileEntityLightOrbController extends TileEntity {
  public static final String ORB_ID_TAG_KEY = "OrbID";
  public static final String ACTIVE_TAG_KEY = "Active";
  public static final String CHECKPOINTS_TAG_KEY = "Checkpoints";
  public static final String LOOPS_TAG_KEY = "Loops";
  public static final String LIGHT_LEVEL_TAG_KEY = "LightLevel";
  public static final String SPEED_TAG_KEY = "Speed";

  private UUID orbID;
  private boolean active;
  private List<PathCheckpoint> checkpoints;
  private boolean loops;
  private int lightLevel;
  /**
   * Movement speed of orb in blocks per second.
   */
  private double speed;

  public TileEntityLightOrbController() {
    this.checkpoints = new LinkedList<>();
  }

  public void init() {
    this.setActive(true);
    this.setLightLevel(15);
    this.setLoops(false);
    this.setSpeed(0.25);
    this.checkpoints = new LinkedList<>();
    this.addCheckpoint(this.pos.up(), true);
    this.spawnOrb();
  }

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

  public void killOrb() {
    if (!this.world.isRemote) {
      EntityLightOrb currentOrb = this.getOrb();
      if (currentOrb != null) {
        this.world.removeEntity(currentOrb);
        this.markDirty();
      }
    }
  }

  private EntityLightOrb getOrb() {
    List<EntityLightOrb> orbs = this.world.getEntities(EntityLightOrb.class, e -> e.getUniqueID().equals(this.orbID));
    return !orbs.isEmpty() ? orbs.get(0) : null;
  }

  public boolean isActive() {
    return this.active;
  }

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
   * Removes the checkpoint at the given index.
   *
   * @param index Index of checkpoint to remove.
   * @return True if the checkpoint was removed,
   * false if the index is invalid or the list contains only one element.
   */
  public boolean removeCheckpoint(int index) {
    if (index >= 0 && index < this.checkpoints.size() && this.checkpoints.size() > 1) {
      this.checkpoints.remove(index);
      return true;
    }
    return false;
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
  public Optional<Pair<Integer, IPathCheckpoint>> getNextCheckpoint(int index) {
    if (index < this.checkpoints.size() - 1) {
      return Optional.of(new ImmutablePair<>(index + 1, this.checkpoints.get(index + 1)));
    } else if (this.loops) {
      return Optional.of(new ImmutablePair<>(0, this.checkpoints.get(0)));
    }
    return Optional.empty();
  }

  public List<IPathCheckpoint> getCheckpoints() {
    return new ArrayList<>(this.checkpoints);
  }

  public boolean loops() {
    return this.loops;
  }

  public void setLoops(boolean loops) {
    this.loops = loops;
    this.markDirty();
  }

  public int getLightLevel() {
    return this.lightLevel;
  }

  public void setLightLevel(int lightLevel) {
    if (lightLevel < 0 || lightLevel > 15) {
      throw new IllegalArgumentException("invalid light value " + lightLevel);
    }
    this.lightLevel = lightLevel;
    this.markDirty();
  }

  public double getSpeed() {
    return this.speed;
  }

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

  private static final class PathCheckpoint implements IPathCheckpoint {
    public static final String POS_TAG_KEY = "Pos";
    public static final String STOP_TAG_KEY = "IsStop";

    public final BlockPos pos;
    public boolean stop;

    public PathCheckpoint(NBTTagCompound tag) {
      this(NBTUtil.getPosFromTag(tag.getCompoundTag(POS_TAG_KEY)), tag.getBoolean(STOP_TAG_KEY));
    }

    public PathCheckpoint(final BlockPos pos, boolean stop) {
      this.pos = pos;
      this.stop = stop;
    }

    @Override
    public BlockPos getPos() {
      return this.pos;
    }

    @Override
    public boolean isStop() {
      return this.stop;
    }

    public NBTTagCompound toNBT() {
      NBTTagCompound tag = new NBTTagCompound();
      tag.setTag(POS_TAG_KEY, NBTUtil.createPosTag(this.pos));
      tag.setBoolean(STOP_TAG_KEY, this.stop);
      return tag;
    }

    @Override
    public String toString() {
      return String.format("PathCheckpoint{pos=%s,stop=%b}", this.pos, this.stop);
    }
  }
}

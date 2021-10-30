package net.darmo_creations.naissancee.tile_entities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

/**
 * Mutable implementation of interface.
 */
public class PathCheckpoint implements Cloneable {
  private static final String POS_TAG_KEY = "Pos";
  private static final String STOP_TAG_KEY = "IsStop";

  private final BlockPos pos;
  private boolean stop;

  /**
   * Create a checkpoint for the given NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public PathCheckpoint(NBTTagCompound tag) {
    this(NBTUtil.getPosFromTag(tag.getCompoundTag(POS_TAG_KEY)), tag.getBoolean(STOP_TAG_KEY));
  }

  /**
   * Create a checkpoint for the given position and stop state.
   *
   * @param pos  Block position.
   * @param stop Whether the light orb should stop at this checkpoint.
   */
  public PathCheckpoint(final BlockPos pos, boolean stop) {
    this.pos = pos;
    this.stop = stop;
  }

  /**
   * The block position of this checkpoint.
   */
  public BlockPos getPos() {
    return this.pos;
  }

  /**
   * Whether the light orb should stop at this checkpoint.
   */
  public boolean isStop() {
    return this.stop;
  }

  /**
   * Set whether the light orb should stop at this checkpoint.
   */
  public void setStop(boolean stop) {
    this.stop = stop;
  }

  /**
   * Convert this checkpoint to an NBT tag.
   */
  public NBTTagCompound toNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setTag(POS_TAG_KEY, NBTUtil.createPosTag(this.pos));
    tag.setBoolean(STOP_TAG_KEY, this.stop);
    return tag;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    PathCheckpoint that = (PathCheckpoint) o;
    return this.stop == that.stop && Objects.equals(this.pos, that.pos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.pos, this.stop);
  }

  @Override
  public PathCheckpoint clone() {
    try {
      return (PathCheckpoint) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return String.format("PathCheckpoint{pos=%s, stop=%b}", this.pos, this.stop);
  }
}

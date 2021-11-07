package net.darmo_creations.naissancee.tile_entities;

import net.darmo_creations.naissancee.blocks.BlockLaserTelemeter;
import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

/**
 * Tile entity for laser telemeter.
 * <p>
 * Negative values for length fields mean that the line should be drawn in the negative direction along its axis.
 *
 * @see BlockLaserTelemeter
 * @see ModBlocks#LASER_TELEMETER
 */
public class TileEntityLaserTelemeter extends TileEntity {
  private static final String SIZE_TAG_KEY = "Size";
  private static final String OFFSET_TAG_KEY = "Offset";

  private Vec3i size;
  private BlockPos offset;

  public TileEntityLaserTelemeter() {
    this.size = new Vec3i(0, 0, 0);
    this.offset = new BlockPos(0, 0, 0);
  }

  public Vec3i getSize() {
    return this.size;
  }

  public void setSize(Vec3i size) {
    this.size = size;
    this.markDirty();
  }

  public BlockPos getOffset() {
    return this.offset;
  }

  public void setOffset(BlockPos offset) {
    this.offset = offset;
    this.markDirty();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setTag(SIZE_TAG_KEY, NBTUtil.createPosTag(new BlockPos(this.size)));
    compound.setTag(OFFSET_TAG_KEY, NBTUtil.createPosTag(this.offset));
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.size = NBTUtil.getPosFromTag(compound.getCompoundTag(SIZE_TAG_KEY));
    this.offset = NBTUtil.getPosFromTag(compound.getCompoundTag(OFFSET_TAG_KEY));
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

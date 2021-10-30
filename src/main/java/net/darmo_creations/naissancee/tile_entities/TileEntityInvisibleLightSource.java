package net.darmo_creations.naissancee.tile_entities;

import net.darmo_creations.naissancee.blocks.BlockInvisibleLightSource;
import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.darmo_creations.naissancee.tile_entities.render.TileEntityInvisibleLightSourceRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Tile entity for {@link ModBlocks#INVISIBLE_LIGHT_SOURCE}.
 * <p>
 * Defines the light level variation behavior of the associated block (cf. {@link EnumMode}).
 *
 * @see TileEntityInvisibleLightSourceRenderer
 * @see BlockInvisibleLightSource
 * @see ModBlocks#INVISIBLE_LIGHT_SOURCE
 */
public class TileEntityInvisibleLightSource extends TileEntity {
  private static final String MODE_KEY = "mode";

  // Current behavior mode
  private EnumMode mode;

  /**
   * Create a tile entity in edit mode.
   */
  public TileEntityInvisibleLightSource() {
    this.setMode(EnumMode.EDIT);
  }

  public EnumMode getMode() {
    return this.mode;
  }

  public void setMode(EnumMode mode) {
    this.mode = mode;
    this.markDirty();
  }

  // Prevent TE from being reset on block state change.
  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    // Check if block is still correct to prevent bugs
    return !(newSate.getBlock() instanceof BlockInvisibleLightSource);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger(MODE_KEY, this.mode.ordinal());
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.mode = EnumMode.values()[compound.getInteger(MODE_KEY)];
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    return this.writeToNBT(new NBTTagCompound());
  }

  /**
   * This enumeration defines behavior modes of the associated block.
   */
  public enum EnumMode {
    /**
     * Light level can be edited.
     */
    EDIT,
    /**
     * Light level is locked to its last value.
     */
    LOCKED,
    /**
     * Light level can only be set by redstone power.
     */
    REDSTONE
  }
}

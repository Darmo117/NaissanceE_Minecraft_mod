package net.darmo_creations.naissancee.tile_entities;

import net.darmo_creations.naissancee.blocks.BlockFloatingVariableLightBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Tile entity for {@link BlockFloatingVariableLightBlock}.
 * Handles light level change.
 */
public class TileEntityFloatingVariableLightBlock extends TileEntity implements ITickable {
  private static final String TIME_TAG_KEY = "Time";
  private static final String INCREASING_TAG_KEY = "Increasing";
  private static final String STOPPED_TAG_KEY = "Stopped";
  private static final String WAIT_NO_COLLISION_TAG_KEY = "WaitForNoCollision";
  private static final String PLAYER_COLLIDING_TAG_KEY = "PlayerColliding";

  public static final int MIN_LIGHT_LEVEL = 4;
  public static final int DELAY = 3;

  private int time;
  private boolean increasing;
  private boolean stopped;
  private boolean waitForNoCollision;
  private boolean playerColliding;

  public TileEntityFloatingVariableLightBlock() {
    this.time = DELAY;
    this.increasing = true;
    this.stopped = true;
    this.waitForNoCollision = true;
    this.playerColliding = false;
  }

  /**
   * Called when a player collides the associated block.
   */
  public void onPlayerColliding() {
    this.playerColliding = true;
  }

  @Override
  public void update() {
    if (this.waitForNoCollision && !this.playerColliding) {
      this.waitForNoCollision = false;
    } else if (this.playerColliding && !this.waitForNoCollision) {
      this.stopped = false;
    }
    if (this.stopped || this.waitForNoCollision) {
      this.playerColliding = false;
      return;
    }

    BlockPos pos = this.getPos();
    IBlockState state = this.world.getBlockState(pos);

    if (state.getBlock() instanceof BlockFloatingVariableLightBlock) {
      int lightLevel = state.getValue(BlockFloatingVariableLightBlock.LIGHT_LEVEL);

      if (this.time == 0) {
        if (this.increasing) {
          if (lightLevel < 15) { // Prevent crash when TE is loading while player is colliding block
            this.world.setBlockState(pos, state.withProperty(BlockFloatingVariableLightBlock.LIGHT_LEVEL, ++lightLevel));
          }
          if (lightLevel == 15) {
            this.increasing = false;
            this.stopped = true;
            if (this.playerColliding) {
              this.waitForNoCollision = true;
            }
          }
        } else {
          if (lightLevel > MIN_LIGHT_LEVEL) { // Prevent crash when TE is loading while player is colliding block
            this.world.setBlockState(pos, state.withProperty(BlockFloatingVariableLightBlock.LIGHT_LEVEL, --lightLevel));
          }
          if (lightLevel == MIN_LIGHT_LEVEL) {
            this.increasing = true;
            this.stopped = true;
            if (this.playerColliding) {
              this.waitForNoCollision = true;
            }
          }
        }
        this.time = DELAY;
      } else {
        if (((this.increasing && lightLevel == MIN_LIGHT_LEVEL) || (!this.increasing && lightLevel == 15)) && this.time == DELAY) {
          this.world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
              SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5f, 1, false);
        }
        this.time--;
      }
    }
    this.playerColliding = false;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.time = compound.getInteger(TIME_TAG_KEY);
    this.increasing = compound.getBoolean(INCREASING_TAG_KEY);
    this.stopped = compound.getBoolean(STOPPED_TAG_KEY);
    this.waitForNoCollision = compound.getBoolean(WAIT_NO_COLLISION_TAG_KEY);
    this.playerColliding = compound.getBoolean(PLAYER_COLLIDING_TAG_KEY);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger(TIME_TAG_KEY, this.time);
    compound.setBoolean(INCREASING_TAG_KEY, this.increasing);
    compound.setBoolean(STOPPED_TAG_KEY, this.stopped);
    compound.setBoolean(WAIT_NO_COLLISION_TAG_KEY, this.waitForNoCollision);
    compound.setBoolean(PLAYER_COLLIDING_TAG_KEY, this.playerColliding);
    return super.writeToNBT(compound);
  }

  // Prevent TE from being reset on block state change.
  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    // Check if block is still correct to prevent bugs
    return !(newSate.getBlock() instanceof BlockFloatingVariableLightBlock);
  }
}

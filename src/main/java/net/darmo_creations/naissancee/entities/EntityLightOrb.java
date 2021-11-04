package net.darmo_creations.naissancee.entities;

import net.darmo_creations.naissancee.Utils;
import net.darmo_creations.naissancee.blocks.BlockLightOrbController;
import net.darmo_creations.naissancee.blocks.BlockLightOrbSource;
import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.darmo_creations.naissancee.tile_entities.PathCheckpoint;
import net.darmo_creations.naissancee.tile_entities.TileEntityLightOrbController;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

/**
 * This class represents the moving light orbs found throughout NaissanceE.
 * <p>
 * Orbs place invisible light-emitting blocks wherever they pass through.
 * If the block an orb is currentry in is not air, no light block is placed.
 * <p>
 * Orbs are controlled by controller blocks (one for each) that hold all necessary data.
 * As such, orb entities do not hold any data except for the next checkpoint they must
 * go to and the position of their controller block.
 * <p>
 * Orbs move in straight lines at constant speed between each checkpoint in their path.
 * <p>
 * Light orbs do not have a rendered model but spawn white smoke particles instead.
 *
 * @see BlockLightOrbSource
 * @see BlockLightOrbController
 * @see TileEntityLightOrbController
 */
public class EntityLightOrb extends Entity {
  public static final String CONTROLLER_POS_TAG_KEY = "ControllerPos";
  public static final String NEXT_CP_INDEX_TAG_KEY = "NextCheckpointIndex";

  /**
   * Position of the associated controller block.
   */
  private static final DataParameter<BlockPos> CONTROLLER_POS = EntityDataManager.createKey(EntityLightOrb.class, DataSerializers.BLOCK_POS);
  /**
   * Index of the next checkpoint; -1 if there is none.
   */
  private static final DataParameter<Integer> NEXT_CHECKPOINT_INDEX = EntityDataManager.createKey(EntityLightOrb.class, DataSerializers.VARINT);
  /**
   * Whether the orb should wait for player collision to move again while stopped.
   */
  private static final DataParameter<Boolean> WAIT_FOR_PLAYER = EntityDataManager.createKey(EntityLightOrb.class, DataSerializers.BOOLEAN);

  // Tile position of this entity, used to place/remove light blocks
  private int tileX;
  private int tileY;
  private int tileZ;

  private int timeToWait;

  @SuppressWarnings("unused")
  public EntityLightOrb(World world) { // Required by Minecraft, invoked on client
    super(world);
  }

  /**
   * Create a light orb entity for the given controller’s tile entity.
   *
   * @param world      The world this entity belongs to.
   * @param controller The controller’s tile entity.
   */
  public EntityLightOrb(World world, TileEntityLightOrbController controller) {
    super(world);
    this.dataManager.set(CONTROLLER_POS, controller.getPos());
  }

  @Override
  protected void entityInit() {
    this.setEntityInvulnerable(true);
    this.setSilent(true);
    this.setNoGravity(true);
    this.setSize(0.25F, 0.25F);
    this.dataManager.register(CONTROLLER_POS, new BlockPos(0, 0, 0));
    this.dataManager.register(NEXT_CHECKPOINT_INDEX, 0);
    this.dataManager.register(WAIT_FOR_PLAYER, true);
  }

  /**
   * Initializes this entity.
   * Actually sets the next checkpoint to the one directly after the start checkpoint if it exists.
   */
  public void init() {
    TileEntityLightOrbController controller = this.controller();
    if (controller != null) { // Controller block may have been removed while world was unloaded
      this.dataManager.set(NEXT_CHECKPOINT_INDEX, controller.getNextCheckpoint(0).map(Pair::getKey).orElse(-1));
    }
  }

  @Override
  public void onUpdate() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;

    this.updateTilePos();

    TileEntityLightOrbController controller = this.controller();
    if (controller == null) { // Account for delay between server and client entity removal
      return;
    }

    if (!controller.isEntityInvisible()) {
      this.world.spawnParticle(EnumParticleTypes.END_ROD, this.posX, this.posY + 0.5, this.posZ, 0, 0, 0);
    }

    BlockPos previousTilePos = this.tilePosToBlockPos();

    if (controller.isActive()) {
      Optional<PathCheckpoint> next = this.nextCheckpoint();
      if (!next.isPresent()) {
        this.stop();
      } else if (!this.isStopped()) {
        PathCheckpoint nextCheckpoint = next.get();
        double nextX = this.posX + this.motionX;
        double nextY = this.posY + this.motionY;
        double nextZ = this.posZ + this.motionZ;
        BlockPos nextPos = new BlockPos(nextX, nextY, nextZ).add(0.5, 0, 0.5);
        BlockPos currentPos = this.posToBlockPos();
        BlockPos nextCheckpointPos = nextCheckpoint.getPos().add(0.5, 0, 0.5);
        boolean reachedNextCP = currentPos.distanceSq(nextPos) > currentPos.distanceSq(nextCheckpointPos);
        if (reachedNextCP) {
          nextX = nextCheckpointPos.getX() + 0.5;
          nextY = nextCheckpointPos.getY();
          nextZ = nextCheckpointPos.getZ() + 0.5;
        }

        this.setPosition(nextX, nextY, nextZ);

        if (reachedNextCP) {
          if (nextCheckpoint.isStop()) {
            this.dataManager.set(WAIT_FOR_PLAYER, true);
            this.stop();
          }
          this.timeToWait = nextCheckpoint.getTicksToWait();
          controller.getNextCheckpoint(this.dataManager.get(NEXT_CHECKPOINT_INDEX)).ifPresent(p -> {
            this.dataManager.set(NEXT_CHECKPOINT_INDEX, p.getKey());
            if (!nextCheckpoint.isStop()) {
              if (this.timeToWait == 0) {
                this.updateMotion(p.getValue());
              } else {
                this.stop();
              }
            }
          });
        }

        this.updateTilePos();
      } else if (this.timeToWait > 0) {
        this.timeToWait--;
        if (this.timeToWait == 0 && !this.dataManager.get(WAIT_FOR_PLAYER)) {
          this.nextCheckpoint().ifPresent(this::updateMotion);
        }
      }
    } else {
      this.stop();
    }

    BlockPos tilePos = this.tilePosToBlockPos();
    this.placeLight(tilePos);
    if (!tilePos.equals(previousTilePos)) {
      this.removeLight(previousTilePos);
    }

    super.onUpdate();
  }

  /**
   * Set this entity’s motion vector to point towards the given checkpoint from the entity’s current position.
   *
   * @param nextCheckpoint The checkpoint to go to.
   */
  private void updateMotion(PathCheckpoint nextCheckpoint) {
    BlockPos currentPos = this.posToBlockPos();
    BlockPos nextCPPos = nextCheckpoint.getPos();
    Vec3i vector = nextCPPos.subtract(currentPos);
    double length = nextCPPos.getDistance(currentPos.getX(), currentPos.getY(), currentPos.getZ());
    double speed = this.controller().getSpeed();
    this.motionX = speed * vector.getX() / length;
    this.motionY = speed * vector.getY() / length;
    this.motionZ = speed * vector.getZ() / length;
  }

  /**
   * Stop this entity.
   */
  private void stop() {
    this.motionX = this.motionY = this.motionZ = 0;
  }

  /**
   * Whether this entity is not moving.
   */
  private boolean isStopped() {
    return this.motionX == 0 && this.motionY == 0 && this.motionZ == 0;
  }

  /**
   * Convert current position fields to BlockPos.
   */
  private BlockPos posToBlockPos() {
    return new BlockPos(this.posX, this.posY, this.posZ);
  }

  /**
   * Convert current tile position fields to BlockPos.
   */
  private BlockPos tilePosToBlockPos() {
    return new BlockPos(this.tileX, this.tileY, this.tileZ);
  }

  /**
   * Update tile position fields using current position.
   */
  private void updateTilePos() {
    // Use Math.floor() to account for negative values
    this.tileX = (int) Math.floor(this.posX);
    this.tileY = (int) Math.floor(this.posY);
    this.tileZ = (int) Math.floor(this.posZ);
  }

  /**
   * Get next checkpoint. Returns null if there is none.
   */
  private Optional<PathCheckpoint> nextCheckpoint() {
    TileEntityLightOrbController controller = this.controller();
    int i = this.dataManager.get(NEXT_CHECKPOINT_INDEX);
    return controller != null && i != -1 && i < controller.getCheckpoints().size()
        ? Optional.of(controller.getCheckpoints().get(i)) : Optional.empty();
  }

  /**
   * Get the controller’s tile entity. May be null during entity initialization.
   */
  private TileEntityLightOrbController controller() {
    return Utils.getTileEntity(TileEntityLightOrbController.class, this.world, this.dataManager.get(CONTROLLER_POS)).orElse(null);
  }

  @Override
  public void onCollideWithPlayer(EntityPlayer entity) {
    super.onCollideWithPlayer(entity);
    this.nextCheckpoint().ifPresent(nextCheckpoint -> {
      if (this.dataManager.get(WAIT_FOR_PLAYER) && this.controller().isActive() && this.isStopped() && this.timeToWait == 0) {
        this.updateMotion(nextCheckpoint);
        this.dataManager.set(WAIT_FOR_PLAYER, false);
      }
    });
  }

  @Override
  public void onRemovedFromWorld() {
    this.removeLight(this.tilePosToBlockPos());
    super.onRemovedFromWorld();
  }

  @Override
  public void readEntityFromNBT(NBTTagCompound compound) {
    this.dataManager.set(CONTROLLER_POS, NBTUtil.getPosFromTag(compound.getCompoundTag(CONTROLLER_POS_TAG_KEY)));
    this.dataManager.set(NEXT_CHECKPOINT_INDEX, compound.getInteger(NEXT_CP_INDEX_TAG_KEY));
  }

  @Override
  public void writeEntityToNBT(NBTTagCompound compound) {
    compound.setTag(CONTROLLER_POS_TAG_KEY, NBTUtil.createPosTag(this.dataManager.get(CONTROLLER_POS)));
    compound.setInteger(NEXT_CP_INDEX_TAG_KEY, this.dataManager.get(NEXT_CHECKPOINT_INDEX));
  }

  @Override
  protected boolean canTriggerWalking() {
    return false;
  }

  /**
   * Place a light block at the given position.
   */
  private void placeLight(BlockPos pos) {
    if (this.world.getBlockState(pos).getBlock() == Blocks.AIR) {
      this.world.setBlockState(pos, ModBlocks.LIGHT_ORB_SOURCE.getDefaultState()
          .withProperty(BlockLightOrbSource.LIGHT_LEVEL, this.controller().getLightLevel()));
    }
  }

  /**
   * Remove light block at given position.
   */
  private void removeLight(BlockPos pos) {
    if (this.world.getBlockState(pos).getBlock() == ModBlocks.LIGHT_ORB_SOURCE) {
      this.world.setBlockToAir(pos);
    }
  }
}

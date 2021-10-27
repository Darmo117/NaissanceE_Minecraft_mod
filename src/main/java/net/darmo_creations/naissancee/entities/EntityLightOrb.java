package net.darmo_creations.naissancee.entities;

import net.darmo_creations.naissancee.Utils;
import net.darmo_creations.naissancee.blocks.BlockLightOrbSource;
import net.darmo_creations.naissancee.blocks.ModBlocks;
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

public class EntityLightOrb extends Entity {
  public static final String CONTROLLER_POS_TAG_KEY = "ControllerPos";
  public static final String NEXT_CP_INDEX_TAG_KEY = "NextCheckpointIndex";

  private static final DataParameter<BlockPos> CONTROLLER_POS = EntityDataManager.createKey(EntityLightOrb.class, DataSerializers.BLOCK_POS);
  /**
   * Index of the next checkpoint; -1 if there is none.
   */
  private static final DataParameter<Integer> NEXT_CHECKPOINT_INDEX = EntityDataManager.createKey(EntityLightOrb.class, DataSerializers.VARINT);

  private int tileX;
  private int tileY;
  private int tileZ;

  @SuppressWarnings("unused")
  public EntityLightOrb(World world) { // Required by Minecraft, invoked on client
    super(world);
  }

  public EntityLightOrb(World world, TileEntityLightOrbController controller) {
    super(world);
    this.dataManager.set(CONTROLLER_POS, controller.getPos());
  }

  @Override
  protected void entityInit() {
    this.setEntityInvulnerable(true);
    this.setSilent(true);
    this.setNoGravity(true);
    this.setSize(0.5F, 0.5F);
    this.dataManager.register(CONTROLLER_POS, new BlockPos(0, 0, 0));
    this.dataManager.register(NEXT_CHECKPOINT_INDEX, 0);
  }

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

    // Negative y speed to compensate for natural rising of particles
    world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY + 0.5, posZ, 0, -0.025, 0);

    BlockPos previousTilePos = this.tilePosToBlockPos();

    if (controller.isActive()) {
      IPathCheckpoint nextCheckpoint = this.nextCheckpoint();
      if (nextCheckpoint == null) {
        this.stop();
      } else if (!this.isStopped()) {
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
            this.stop();
          }
          controller.getNextCheckpoint(this.dataManager.get(NEXT_CHECKPOINT_INDEX)).ifPresent(p -> {
            this.dataManager.set(NEXT_CHECKPOINT_INDEX, p.getKey());
            if (!nextCheckpoint.isStop()) {
              this.updateMotion(p.getValue());
            }
          });
        }

        this.updateTilePos();
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

  private void updateMotion(IPathCheckpoint nextCheckpoint) {
    BlockPos currentPos = this.posToBlockPos();
    BlockPos nextCPPos = nextCheckpoint.getPos();
    Vec3i vector = nextCPPos.subtract(currentPos);
    double length = nextCPPos.getDistance(currentPos.getX(), currentPos.getY(), currentPos.getZ());
    double speed = this.controller().getSpeed();
    this.motionX = speed * vector.getX() / length;
    this.motionY = speed * vector.getY() / length;
    this.motionZ = speed * vector.getZ() / length;
  }

  private void stop() {
    this.motionX = this.motionY = this.motionZ = 0;
  }

  private boolean isStopped() {
    return this.motionX == 0 && this.motionY == 0 && this.motionZ == 0;
  }

  private BlockPos posToBlockPos() {
    return new BlockPos(this.posX, this.posY, this.posZ);
  }

  private BlockPos tilePosToBlockPos() {
    return new BlockPos(this.tileX, this.tileY, this.tileZ);
  }

  private void updateTilePos() {
    // Use Math.floor() to account for negative values
    this.tileX = (int) Math.floor(this.posX);
    this.tileY = (int) Math.floor(this.posY);
    this.tileZ = (int) Math.floor(this.posZ);
  }

  private IPathCheckpoint nextCheckpoint() {
    TileEntityLightOrbController controller = this.controller();
    int i = this.dataManager.get(NEXT_CHECKPOINT_INDEX);
    return controller != null && i != -1 && i < controller.getCheckpoints().size()
        ? controller.getCheckpoints().get(i) : null;
  }

  private TileEntityLightOrbController controller() {
    return Utils.getTileEntity(TileEntityLightOrbController.class, this.world, this.dataManager.get(CONTROLLER_POS)).orElse(null);
  }

  @Override
  public void onCollideWithPlayer(EntityPlayer entity) {
    super.onCollideWithPlayer(entity);
    IPathCheckpoint nextCheckpoint = this.nextCheckpoint();
    if (nextCheckpoint != null && this.controller().isActive() && this.isStopped()) {
      this.updateMotion(nextCheckpoint);
    }
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

  private void placeLight(BlockPos pos) {
    if (this.world.getBlockState(pos).getBlock() == Blocks.AIR) {
      this.world.setBlockState(pos, ModBlocks.LIGHT_ORB_SOURCE.getDefaultState()
          .withProperty(BlockLightOrbSource.LIGHT_LEVEL, this.controller().getLightLevel()));
    }
  }

  private void removeLight(BlockPos pos) {
    if (this.world.getBlockState(pos).getBlock() == ModBlocks.LIGHT_ORB_SOURCE) {
      this.world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }
  }
}

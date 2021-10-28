package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * A light-emitting block that emits a redstone signal when a player enters in contact with it.
 */
public class BlockActivatorLamp extends Block implements IModBlock {
  // Bounding boxes are slightly smaller/bigger than a full block to enable player detection.
  private static final AxisAlignedBB COLLISION_AABB = new AxisAlignedBB(0.002, 0.002, 0.002, 0.998, 0.998, 0.998);
  private static final AxisAlignedBB DETECTION_AABB = new AxisAlignedBB(-0.002, -0.002, -0.002, 1.002, 1.002, 1.002);

  public static final PropertyBool POWERED = PropertyBool.create("powered");

  public BlockActivatorLamp() {
    super(Material.ROCK);
    this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false));
    this.setLightLevel(1.0f);
    this.setTickRandomly(true);
  }

  @SuppressWarnings("deprecation")
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return DETECTION_AABB;
  }

  @SuppressWarnings("deprecation")
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return COLLISION_AABB;
  }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    this.updateState(world, pos, state);
  }

  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity) {
    this.updateState(world, pos, world.getBlockState(pos));
  }

  @Override
  public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
    this.updateState(world, pos, state);
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (this.getRedstoneStrength(state) > 0) {
      this.updateNeighbors(worldIn, pos);
    }
    super.breakBlock(worldIn, pos, state);
  }

  @Override
  @SuppressWarnings("deprecation")
  public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return this.getRedstoneStrength(blockState);
  }

  @Override
  @SuppressWarnings("deprecation")
  public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return this.getRedstoneStrength(blockState);
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean canProvidePower(IBlockState state) {
    return true;
  }

  @Override
  @SuppressWarnings("deprecation")
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(POWERED, meta != 0);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(POWERED) ? 1 : 0;
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, POWERED);
  }

  @Override
  public int tickRate(World world) {
    return 10;
  }

  /**
   * Update redstone power state.
   *
   * @param world World the block is in.
   * @param pos   Block’s position.
   * @param state Block’s current state.
   */
  private void updateState(World world, BlockPos pos, IBlockState state) {
    if (!world.isRemote) {
      int currentRedstoneStrength = this.getRedstoneStrength(state);
      int strength = this.computeRedstoneStrength(world, pos, state);

      if (currentRedstoneStrength != strength) {
        world.setBlockState(pos, state.withProperty(POWERED, strength != 0), 2);
        this.updateNeighbors(world, pos);
        world.markBlockRangeForRenderUpdate(pos, pos);
      }

      if (strength != 0) {
        world.scheduleUpdate(pos, this, this.tickRate(world));
      }
    }
  }

  /**
   * Compute redstone signal strength.
   *
   * @param world Current world.
   * @param pos   Block’s position.
   * @param state Current block state.
   * @return Redstone signal strength.
   */
  private int computeRedstoneStrength(World world, BlockPos pos, IBlockState state) {
    return world.getEntitiesWithinAABB(EntityPlayer.class, state.getBoundingBox(world, pos).offset(pos)).isEmpty() ? 0 : 15;
  }

  /**
   * Update all neighboring blocks.
   *
   * @param world Current world.
   * @param pos   Block’s position.
   */
  private void updateNeighbors(World world, BlockPos pos) {
    world.notifyNeighborsOfStateChange(pos, this, true);
    for (EnumFacing side : EnumFacing.values()) {
      world.notifyNeighborsOfStateChange(pos.offset(side), this, false);
    }
  }

  /**
   * Get redstone signal strength for block state.
   */
  private int getRedstoneStrength(IBlockState state) {
    return state.getValue(POWERED) ? 15 : 0;
  }
}

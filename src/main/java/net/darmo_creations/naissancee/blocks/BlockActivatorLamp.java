package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockActivatorLamp extends Block implements IModBlock {
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
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!worldIn.isRemote) {
      int strength = this.getRedstoneStrength(state);
      if (strength != 0) {
        this.updateState(worldIn, pos, state, strength);
      }
    }
  }

  @Override
  public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
    if (!worldIn.isRemote) {
      IBlockState state = worldIn.getBlockState(pos);
      int strength = this.getRedstoneStrength(state);
      if (strength == 0) {
        this.updateState(worldIn, pos, state, strength);
      }
    }
  }

  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    if (!worldIn.isRemote) {
      int strength = this.getRedstoneStrength(state);
      if (strength == 0) {
        this.updateState(worldIn, pos, state, strength);
      }
    }
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
  public int tickRate(World worldIn) {
    return 10;
  }

  protected void updateState(World worldIn, BlockPos pos, IBlockState state, int oldRedstoneStrength) {
    int strength = this.computeRedstoneStrength(worldIn, pos, state);

    if (oldRedstoneStrength != strength) {
      worldIn.setBlockState(pos, state.withProperty(POWERED, strength != 0), 2);
      this.updateNeighbors(worldIn, pos);
      worldIn.markBlockRangeForRenderUpdate(pos, pos);
    }

    if (strength != 0) {
      worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
    }
  }

  protected int computeRedstoneStrength(World worldIn, BlockPos pos, IBlockState state) {
    return worldIn.getEntitiesWithinAABB(EntityPlayer.class, state.getBoundingBox(worldIn, pos).offset(pos)).isEmpty() ? 0 : 15;
  }

  protected void updateNeighbors(World worldIn, BlockPos pos) {
    worldIn.notifyNeighborsOfStateChange(pos, this, true);
    for (EnumFacing side : EnumFacing.values()) {
      worldIn.notifyNeighborsOfStateChange(pos.offset(side), this, false);
    }
  }

  protected int getRedstoneStrength(IBlockState state) {
    return state.getValue(POWERED) ? 15 : 0;
  }
}

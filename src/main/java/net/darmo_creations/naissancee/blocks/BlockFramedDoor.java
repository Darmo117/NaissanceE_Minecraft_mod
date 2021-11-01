package net.darmo_creations.naissancee.blocks;

import net.darmo_creations.naissancee.items.ItemFramedDoor;
import net.darmo_creations.naissancee.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * An opaque door with a frame that retracts upwards when opened.
 *
 * @see ItemFramedDoor
 */
public class BlockFramedDoor extends Block implements IModBlock {
  public static final double UNIT = 0.125;

  public static final PropertyBool OPEN = PropertyBool.create("open");
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  public static final PropertyEnum<BlockDoor.EnumDoorHalf> HALF = PropertyEnum.create("half", BlockDoor.EnumDoorHalf.class);
  public static final PropertyEnum<EnumDirection> DIRECTION = PropertyEnum.create("direction", EnumDirection.class);

  // Door
  private static final AxisAlignedBB NORTH_SOUTH_DOOR_CLOSED_AABB = new AxisAlignedBB(0, 0, 0.5 - UNIT / 2, 1, 1, 0.5 + UNIT / 2);
  private static final AxisAlignedBB EAST_WEST_DOOR_CLOSED_AABB = new AxisAlignedBB(0.5 - UNIT / 2, 0, 0, 0.5 + UNIT / 2, 1, 1);
  private static final AxisAlignedBB NORTH_SOUTH_DOOR_TOP_OPEN_AABB = new AxisAlignedBB(0, 1 - UNIT / 2, 0.5 - UNIT / 2, 1, 1, 0.5 + UNIT / 2);
  private static final AxisAlignedBB EAST_WEST_DOOR_TOP_OPEN_AABB = new AxisAlignedBB(0.5 - UNIT / 2, 1 - UNIT / 2, 0, 0.5 + UNIT / 2, 1, 1);
  // Frame
  private static final AxisAlignedBB NORTH_SOUTH_FRAME_LEFT_AABB = new AxisAlignedBB(1, 0, -UNIT, 1 + UNIT, 1, 1 + UNIT);
  private static final AxisAlignedBB NORTH_SOUTH_FRAME_RIGHT_AABB = new AxisAlignedBB(-UNIT, 0, -UNIT, 0, 1, 1 + UNIT);
  private static final AxisAlignedBB NORTH_SOUTH_FRAME_TOP_AABB = new AxisAlignedBB(-UNIT, 1, -UNIT, 1 + UNIT, 1 + UNIT, 1 + UNIT);
  private static final AxisAlignedBB EAST_WEST_FRAME_LEFT_AABB = new AxisAlignedBB(-UNIT, 0, -UNIT, 1 + UNIT, 1, 0);
  private static final AxisAlignedBB EAST_WEST_FRAME_RIGHT_AABB = new AxisAlignedBB(-UNIT, 0, 1, 1 + UNIT, 1, 1 + UNIT);

  public BlockFramedDoor() {
    super(Material.ROCK);
  }

  @SuppressWarnings("deprecation")
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
    state = state.getActualState(world, pos);
    if (state.getValue(OPEN)) {
      if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER) {
        return NULL_AABB;
      } else {
        if (state.getValue(DIRECTION) == EnumDirection.NORTH_SOUTH) {
          return NORTH_SOUTH_DOOR_TOP_OPEN_AABB;
        } else {
          return EAST_WEST_DOOR_TOP_OPEN_AABB;
        }
      }
    } else {
      if (state.getValue(DIRECTION) == EnumDirection.NORTH_SOUTH) {
        return NORTH_SOUTH_DOOR_CLOSED_AABB;
      } else {
        return EAST_WEST_DOOR_CLOSED_AABB;
      }
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity, boolean isActualState) {
    super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entity, isActualState);
    IBlockState actualState = state.getActualState(world, pos);
    if (actualState.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
      addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_SOUTH_FRAME_TOP_AABB);
    }
    if (actualState.getValue(DIRECTION) == EnumDirection.NORTH_SOUTH) {
      addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_SOUTH_FRAME_RIGHT_AABB);
      addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_SOUTH_FRAME_LEFT_AABB);
    } else {
      addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_WEST_FRAME_RIGHT_AABB);
      addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_WEST_FRAME_LEFT_AABB);
    }
  }

  @Override
  public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  public boolean isPassable(IBlockAccess world, BlockPos pos) {
    return world.getBlockState(pos).getActualState(world, pos).getValue(OPEN);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isTopSolid(IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideSolid(IBlockState baseState, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return false;
  }

  private int getCloseSound() {
    return 1011;
  }

  private int getOpenSound() {
    return 1005;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
    if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
      BlockPos posLowerHalf = pos.down();
      IBlockState stateLowerHalf = world.getBlockState(posLowerHalf);

      if (stateLowerHalf.getBlock() != this) {
        world.setBlockToAir(pos);
      } else if (block != this) {
        stateLowerHalf.neighborChanged(world, posLowerHalf, block, fromPos);
      }
    } else {
      boolean destroyed = false;
      BlockPos posUpperHalf = pos.up();
      IBlockState stateUpperHalf = world.getBlockState(posUpperHalf);

      if (stateUpperHalf.getBlock() != this) {
        world.setBlockToAir(pos);
        destroyed = true;
      }

      if (!world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP)) {
        world.setBlockToAir(pos);
        destroyed = true;
        if (stateUpperHalf.getBlock() == this) {
          world.setBlockToAir(posUpperHalf);
        }
      }

      if (destroyed) {
        if (!world.isRemote) {
          this.dropBlockAsItem(world, pos, state, 0);
        }
      } else {
        boolean powered = world.isBlockPowered(pos) || world.isBlockPowered(posUpperHalf);

        if (block != this && (powered || block.getDefaultState().canProvidePower()) && powered != stateUpperHalf.getValue(POWERED)) {
          world.setBlockState(posUpperHalf, stateUpperHalf.withProperty(POWERED, powered), 2);

          if (powered != state.getValue(OPEN)) {
            world.setBlockState(pos, state.withProperty(OPEN, powered), 2);
            world.markBlockRangeForRenderUpdate(pos, pos);
            world.playEvent(null, powered ? this.getOpenSound() : this.getCloseSound(), pos, 0);
          }
        }
      }
    }
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? Items.AIR : ModItems.LIGHT_GRAY_FRAMED_DOOR;
  }

  @Override
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    if (pos.getY() >= worldIn.getHeight() - 1) {
      return false;
    } else {
      IBlockState state = worldIn.getBlockState(pos.down());
      //noinspection deprecation
      return (state.isTopSolid() || state.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID)
          && super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up());
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public EnumPushReaction getMobilityFlag(IBlockState state) {
    return EnumPushReaction.DESTROY;
  }

  @SuppressWarnings("deprecation")
  @Override
  public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
    return new ItemStack(ModItems.LIGHT_GRAY_FRAMED_DOOR);
  }

  @Override
  public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    BlockPos posDown = pos.down();
    BlockPos posUp = pos.up();

    if (player.capabilities.isCreativeMode && state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER && worldIn.getBlockState(posDown).getBlock() == this) {
      worldIn.setBlockToAir(posDown);
    }

    if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER && worldIn.getBlockState(posUp).getBlock() == this) {
      if (player.capabilities.isCreativeMode) {
        worldIn.setBlockToAir(pos);
      }
      worldIn.setBlockToAir(posUp);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER) {
      IBlockState stateUp = world.getBlockState(pos.up());
      if (stateUp.getBlock() == this) {
        state = state.withProperty(POWERED, stateUp.getValue(POWERED));
      }
    } else {
      IBlockState stateDown = world.getBlockState(pos.down());
      if (stateDown.getBlock() == this) {
        state = state.withProperty(DIRECTION, stateDown.getValue(DIRECTION))
            .withProperty(OPEN, stateDown.getValue(OPEN));
      }
    }

    return state;
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.getValue(HALF) != BlockDoor.EnumDoorHalf.LOWER ? state : state.withProperty(DIRECTION, state.getValue(DIRECTION).withRotation(rot));
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getStateFromMeta(int meta) {
    return (meta & 8) != 0
        ? this.getDefaultState()
        .withProperty(HALF, BlockDoor.EnumDoorHalf.UPPER)
        .withProperty(POWERED, (meta & 1) != 0)
        : this.getDefaultState()
        .withProperty(HALF, BlockDoor.EnumDoorHalf.LOWER)
        .withProperty(DIRECTION, (meta & 4) == 0 ? EnumDirection.NORTH_SOUTH : EnumDirection.EAST_WEST)
        .withProperty(OPEN, (meta & 2) != 0);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    int i = 0;

    if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
      i = i | 8;
      if (state.getValue(POWERED)) {
        i |= 1;
      }
    } else {
      i = i | (4 * state.getValue(DIRECTION).ordinal());
      if (state.getValue(OPEN)) {
        i |= 2;
      }
    }

    return i;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, OPEN, POWERED, DIRECTION, HALF);
  }

  @Override
  public boolean hasGeneratedItemBlock() {
    return false;
  }

  public enum EnumDirection implements IStringSerializable {
    NORTH_SOUTH("north_south"),
    EAST_WEST("east_west");

    private final String name;

    EnumDirection(String name) {
      this.name = name;
    }

    public EnumDirection withRotation(Rotation rotation) {
      if (this == NORTH_SOUTH) {
        return rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180 ? NORTH_SOUTH : EAST_WEST;
      } else {
        return rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180 ? EAST_WEST : NORTH_SOUTH;
      }
    }

    @Override
    public String getName() {
      return this.name;
    }

    @Override
    public String toString() {
      return this.getName();
    }

    public static EnumDirection fromAngle(double angle) {
      return values()[MathHelper.floor(angle / 90 + 0.5) & 1];
    }
  }
}

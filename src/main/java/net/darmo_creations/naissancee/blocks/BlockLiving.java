package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * This class reprensents a part of the striped black-and-white blocks that are afraid of light in NaissanceE.
 * <p>
 * Its texture can connect to other adjacent blocks of the same class.
 */
public class BlockLiving extends Block {
  // Used for connected textures
  public static final PropertyBool NORTH = PropertyBool.create("north");
  public static final PropertyBool SOUTH = PropertyBool.create("south");
  public static final PropertyBool WEST = PropertyBool.create("west");
  public static final PropertyBool EAST = PropertyBool.create("east");

  public BlockLiving() {
    super(Material.IRON);
    this.setSoundType(SoundType.METAL);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, NORTH, SOUTH, WEST, EAST);
  }

  @Override
  @SuppressWarnings("deprecation")
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    return state.withProperty(NORTH, this.canBlockConnectTo(world, pos, EnumFacing.NORTH))
        .withProperty(EAST, this.canBlockConnectTo(world, pos, EnumFacing.EAST))
        .withProperty(SOUTH, this.canBlockConnectTo(world, pos, EnumFacing.SOUTH))
        .withProperty(WEST, this.canBlockConnectTo(world, pos, EnumFacing.WEST));
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return 0;
  }

  @Override
  @SuppressWarnings("deprecation")
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    switch (rot) {
      case CLOCKWISE_180:
        return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
      case COUNTERCLOCKWISE_90:
        return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
      case CLOCKWISE_90:
        return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
      default:
        return state;
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public IBlockState withMirror(IBlockState state, Mirror mirror) {
    switch (mirror) {
      case LEFT_RIGHT:
        return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
      case FRONT_BACK:
        return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
      default:
        return super.withMirror(state, mirror);
    }
  }

  @Override
  public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
    return this.canConnectTo(world, pos.offset(facing));
  }

  /**
   * Whether the block at the given position should connect its texture to its neighbor on the given side.
   */
  private boolean canBlockConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
    BlockPos other = pos.offset(facing);
    Block block = world.getBlockState(other).getBlock();
    return block.canBeConnectedTo(world, other, facing.getOpposite()) || this.canConnectTo(world, other);
  }

  /**
   * Whether a block of this class should connect its textures to the block type at the given position.
   */
  public boolean canConnectTo(IBlockAccess world, BlockPos pos) {
    return world.getBlockState(pos).getBlock() instanceof BlockLiving;
  }
}

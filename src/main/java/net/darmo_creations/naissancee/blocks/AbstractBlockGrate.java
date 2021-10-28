package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Base class for grate blocks.
 * <p>
 * A grate is a block that features aligned vertical rectangular poles.
 * It can face any of the four horizontal directions.
 */
public abstract class AbstractBlockGrate extends BlockHorizontal implements IModBlock {
  public static final PropertyEnum<AbstractBlockGrate.EnumDirection> DIRECTION = PropertyEnum.create("direction", AbstractBlockGrate.EnumDirection.class);

  /**
   * Create a grate block with the given material, color and bounding boxes.
   *
   * @param material Block’s material.
   * @param color    Block’s map color.
   */
  public AbstractBlockGrate(Material material, MapColor color) {
    super(material, color);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING, DIRECTION);
  }

  @SuppressWarnings("deprecation")
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return this.getBoundingBox(state.getValue(FACING), state.getValue(DIRECTION));
  }

  /**
   * Return the bounding box for the given facing and direction.
   *
   * @param facing    Facing direction. Will only be either NORTH, SOUTH, WEST or EAST.
   * @param direction Grate direction.
   * @return The bounding box.
   */
  protected abstract AxisAlignedBB getBoundingBox(EnumFacing facing, EnumDirection direction);

  @SuppressWarnings("deprecation")
  @Override
  public boolean isTopSolid(IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
    return this.isSideSolid(state, world, pos, face) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    EnumFacing playerFacing = placer.getHorizontalFacing().getOpposite();
    IBlockState iblockstate = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer)
        .withProperty(FACING, playerFacing);
    EnumDirection direction;
    switch (playerFacing) {
      case NORTH:
        direction = hitX > 0.5 ? EnumDirection.LEFT_RIGHT : EnumDirection.RIGHT_LEFT;
        break;
      case SOUTH:
        direction = hitX < 0.5 ? EnumDirection.LEFT_RIGHT : EnumDirection.RIGHT_LEFT;
        break;
      case WEST:
        direction = hitZ < 0.5 ? EnumDirection.LEFT_RIGHT : EnumDirection.RIGHT_LEFT;
        break;
      case EAST:
        direction = hitZ > 0.5 ? EnumDirection.LEFT_RIGHT : EnumDirection.RIGHT_LEFT;
        break;
      default:
        // Should not happen
        throw new IllegalArgumentException("Invalid player facing: " + playerFacing);
    }
    return iblockstate.withProperty(DIRECTION, direction);
  }

  @Override
  public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public IBlockState getStateFromMeta(int meta) {
    IBlockState iblockstate = this.getDefaultState().withProperty(DIRECTION, (meta & 4) != 0 ? EnumDirection.RIGHT_LEFT : EnumDirection.LEFT_RIGHT);
    return iblockstate.withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    if (state.getValue(DIRECTION) == EnumDirection.RIGHT_LEFT) {
      i |= 4;
    }
    return i | (state.getValue(FACING).getHorizontalIndex());
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState withMirror(IBlockState state, Mirror mirror) {
    return state.withRotation(mirror.toRotation(state.getValue(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }

  /**
   * This enum defines the two directions of grate blocks.
   */
  public enum EnumDirection implements IStringSerializable {
    LEFT_RIGHT("left_right"),
    RIGHT_LEFT("right_left");

    private final String name;

    EnumDirection(final String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }

    @Override
    public String getName() {
      return this.name;
    }
  }
}

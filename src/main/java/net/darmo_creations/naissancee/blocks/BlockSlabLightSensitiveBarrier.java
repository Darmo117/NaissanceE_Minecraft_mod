package net.darmo_creations.naissancee.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * This block represents a light-sensitive slab.
 */
public abstract class BlockSlabLightSensitiveBarrier extends BlockSlab implements IModBlock {
  public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

  private final boolean passable;

  /**
   * Create a light sensitive barrier slab.
   *
   * @param passable Whether players can pass through it.
   */
  public BlockSlabLightSensitiveBarrier(final boolean passable) {
    super(Material.ROCK);
    this.passable = passable;
    IBlockState state = this.blockState.getBaseState();
    if (!this.isDouble()) {
      state = state.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
    }
    this.setDefaultState(state.withProperty(VARIANT, EnumType.WHITE));
  }

  @SuppressWarnings("deprecation")
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {
    return this.passable ? NULL_AABB : super.getCollisionBoundingBox(blockState, world, pos);
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    Block block = this.passable ? ModBlocks.LIGHT_SENSITIVE_BARRIER_SLAB_PASSABLE : ModBlocks.LIGHT_SENSITIVE_BARRIER_SLAB;
    return Item.getItemFromBlock(block);
  }

  @SuppressWarnings("deprecation")
  @Override
  public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
    Block block = this.passable ? ModBlocks.LIGHT_SENSITIVE_BARRIER_SLAB_PASSABLE : ModBlocks.LIGHT_SENSITIVE_BARRIER_SLAB;
    return new ItemStack(block, 1, state.getValue(VARIANT).ordinal());
  }

  @Override
  public String getUnlocalizedName(int meta) {
    return super.getUnlocalizedName() + "." + EnumType.values()[(meta & 7) % EnumType.values().length].getName();
  }

  @Override
  public IProperty<?> getVariantProperty() {
    return VARIANT;
  }

  @Override
  public Comparable<?> getTypeForItem(ItemStack stack) {
    return EnumType.values()[(stack.getMetadata() & 7) % EnumType.values().length];
  }

  @Override
  public void getSubBlocks(CreativeTabs item, NonNullList<ItemStack> items) {
    for (EnumType type : EnumType.values()) {
      items.add(new ItemStack(this, 1, type.ordinal()));
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getStateFromMeta(int meta) {
    IBlockState state = this.getDefaultState().withProperty(VARIANT, EnumType.values()[(meta & 7) % EnumType.values().length]);
    if (!this.isDouble()) {
      state = state.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
    }
    return state;
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    int i = state.getValue(VARIANT).ordinal();

    if (!this.isDouble() && state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
      i |= 8;
    }

    return i;
  }

  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
    if (this.passable) {
      return BlockFaceShape.UNDEFINED;
    }
    return this.isDouble() ? BlockFaceShape.SOLID : super.getBlockFaceShape(world, state, pos, face);
  }

  @Override
  public boolean isTopSolid(IBlockState state) {
    return !this.passable && super.isTopSolid(state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return !this.passable && super.isSideSolid(base_state, world, pos, side);
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return !this.passable && super.isOpaqueCube(state);
  }

  @Override
  public boolean isFullCube(IBlockState state) {
    return !this.passable && super.isFullCube(state);
  }

  @Override
  public BlockRenderLayer getBlockLayer() {
    return this.passable ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.SOLID;
  }

  @Override
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return this.passable || super.shouldSideBeRendered(blockState, blockAccess, pos, side);
  }

  @Override
  public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
    return !this.passable && super.doesSideBlockRendering(state, world, pos, face);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return this.isDouble() ? new BlockStateContainer(this, VARIANT) : new BlockStateContainer(this, HALF, VARIANT);
  }

  @Override
  public int damageDropped(IBlockState state) {
    return state.getValue(VARIANT).ordinal();
  }

  @Override
  public boolean hasGeneratedItemBlock() {
    return false;
  }

  /**
   * Half slab class.
   */
  public static class BlockHalfSlabLightSensitiveBarrier extends BlockSlabLightSensitiveBarrier {
    /**
     * Create a light sensitive barrier half slab.
     *
     * @param passable Whether players can pass through it.
     */
    public BlockHalfSlabLightSensitiveBarrier(final boolean passable) {
      super(passable);
    }

    @Override
    public boolean isDouble() {
      return false;
    }
  }

  /**
   * Double slab class.
   */
  public static class BlockDoubleSlabLightSensitiveBarrier extends BlockSlabLightSensitiveBarrier {
    /**
     * Create a light sensitive barrier double slab.
     *
     * @param passable Whether players can pass through it.
     */
    public BlockDoubleSlabLightSensitiveBarrier(final boolean passable) {
      super(passable);
    }

    @Override
    public boolean isDouble() {
      return true;
    }
  }

  /**
   * Variants enumeration.
   */
  public enum EnumType implements IStringSerializable {
    WHITE("white"),
    LIGHT_GRAY("light_gray"),
    GRAY("gray");

    private final String name;

    EnumType(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return this.name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }
}

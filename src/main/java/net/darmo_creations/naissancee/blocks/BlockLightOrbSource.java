package net.darmo_creations.naissancee.blocks;

import net.darmo_creations.naissancee.entities.EntityLightOrb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * An invisible, light-emitting block placed by light orb entities.
 *
 * @see EntityLightOrb
 */
public class BlockLightOrbSource extends BlockVariableLightSource {
  public BlockLightOrbSource() {
    super(Material.AIR);
    this.translucent = true;
  }

  @Override
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return NULL_AABB;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
    return false;
  }

  @Override
  public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
  }

  @Override
  public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
    return true;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @Override
  @SuppressWarnings("deprecation")
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.INVISIBLE;
  }

  @Override
  public boolean hasGeneratedItemBlock() {
    return false;
  }
}

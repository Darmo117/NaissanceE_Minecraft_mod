package net.darmo_creations.naissancee.items;

import net.darmo_creations.naissancee.blocks.BlockFramedDoor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Item for {@link BlockFramedDoor}.
 */
public class ItemFramedDoor extends Item {
  private final Block block;

  public ItemFramedDoor(Block block) {
    this.block = block;
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (facing != EnumFacing.UP) {
      return EnumActionResult.FAIL;
    } else {
      Block block = world.getBlockState(pos).getBlock();

      if (!block.isReplaceable(world, pos)) {
        pos = pos.offset(facing);
      }

      ItemStack itemstack = player.getHeldItem(hand);

      if (player.canPlayerEdit(pos, facing, itemstack) && this.block.canPlaceBlockAt(world, pos)) {
        BlockFramedDoor.EnumDirection direction = BlockFramedDoor.EnumDirection.fromAngle(player.rotationYaw);
        placeDoor(world, pos, direction, this.block);
        SoundType soundtype = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos), world, pos, player);
        world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
            (soundtype.getVolume() + 1F) / 2F, soundtype.getPitch() * 0.8F);
        itemstack.shrink(1);
        return EnumActionResult.SUCCESS;
      } else {
        return EnumActionResult.FAIL;
      }
    }
  }

  public static void placeDoor(World world, BlockPos pos, BlockFramedDoor.EnumDirection direction, Block door) {
    BlockPos posUp = pos.up();
    boolean powered = world.isBlockPowered(pos) || world.isBlockPowered(posUp);
    IBlockState state = door.getDefaultState()
        .withProperty(BlockFramedDoor.DIRECTION, direction)
        .withProperty(BlockFramedDoor.POWERED, powered)
        .withProperty(BlockFramedDoor.OPEN, powered);

    world.setBlockState(pos, state.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 2);
    world.setBlockState(posUp, state.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);
    world.notifyNeighborsOfStateChange(pos, door, false);
    world.notifyNeighborsOfStateChange(posUp, door, false);
  }
}

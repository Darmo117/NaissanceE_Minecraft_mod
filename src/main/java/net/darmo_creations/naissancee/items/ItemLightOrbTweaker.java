package net.darmo_creations.naissancee.items;

import net.darmo_creations.naissancee.Utils;
import net.darmo_creations.naissancee.blocks.BlockLightOrbController;
import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.darmo_creations.naissancee.tile_entities.PathCheckpoint;
import net.darmo_creations.naissancee.tile_entities.TileEntityLightOrbController;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

/**
 * Item used to edit light orbs.
 * This item is not stackable.
 * <p>
 * Usage:
 * <li>Sneak-right-click on a controller block to select it.
 * Corresponding path checkpoints will then be highlighted while holding this item.
 * <li>Right-click a controller block to open its configuration GUI.
 * <li>Right-click on a block to add a checkpoint at the block adjacent to the clicked side.
 * <li>Right-click while sneaking on a checkpoint to remove it.
 *
 * @see BlockLightOrbController
 * @see TileEntityLightOrbController
 */
public class ItemLightOrbTweaker extends Item {
  private static final String CONTROLLER_POS_TAG_KEY = "ControllerPos";

  public ItemLightOrbTweaker() {
    this.setMaxStackSize(1);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (world.getBlockState(pos).getBlock() == ModBlocks.LIGHT_ORB_CONTROLLER) {
      if (player.isSneaking()) {
        Optional<TileEntityLightOrbController> te = Utils.getTileEntity(TileEntityLightOrbController.class, world, pos);
        if (te.isPresent()) {
          NBTTagCompound tag = new NBTTagCompound();
          tag.setTag(CONTROLLER_POS_TAG_KEY, NBTUtil.createPosTag(te.get().getPos()));
          player.getHeldItem(hand).setTagCompound(tag);
          return EnumActionResult.SUCCESS;
        }
      }
    } else {
      TileEntityLightOrbController controller = getControllerTileEntity(player.getHeldItem(hand), world);
      if (controller != null) {
        boolean success = true;
        if (player.isSneaking()) {
          BlockPos p = pos.offset(facing);
          if (controller.hasCheckpointAt(p)) {
            int nbRemoved = controller.removeCheckpoint(p);
            success = nbRemoved != 0;
            String s = nbRemoved > 1 ? "s" : "";
            if (nbRemoved == 0) {
              Utils.sendMessage(world, player, new TextComponentString("Cannot remove this checkpoint!")
                  .setStyle(new Style().setColor(TextFormatting.RED)));
            } else {
              Utils.sendMessage(world, player, new TextComponentString(
                  String.format("Removed %d checkpoint%s.", nbRemoved, s))
                  .setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
            }
          } else {
            success = false;
          }
        } else {
          controller.addCheckpoint(pos.offset(facing), true);
        }
        if (success) {
          return EnumActionResult.SUCCESS;
        }
      }
    }
    return EnumActionResult.FAIL;
  }

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
    super.onUpdate(stack, world, entity, itemSlot, isSelected);
    if (world.isRemote) {
      TileEntityLightOrbController te = getControllerTileEntity(stack, world);
      if (isSelected && te != null) {
        for (PathCheckpoint checkpoint : te.getCheckpoints()) {
          BlockPos pos = checkpoint.getPos();
          EnumParticleTypes particleType = checkpoint.isStop() ? EnumParticleTypes.REDSTONE : EnumParticleTypes.DRAGON_BREATH;
          world.spawnParticle(particleType, true, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
        }
      } else if (te == null && stack.getTagCompound() != null && stack.getTagCompound().hasKey(CONTROLLER_POS_TAG_KEY)) {
        stack.getTagCompound().removeTag(CONTROLLER_POS_TAG_KEY);
      }
    }
  }

  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
    TileEntityLightOrbController controller = getControllerTileEntity(stack, world);
    if (controller != null) {
      tooltip.add("Selected controller position: " + Utils.blockPosToString(controller.getPos()));
    } else {
      tooltip.add(TextFormatting.ITALIC + "No controller selected");
    }
  }

  /**
   * Return the tile entity for the controller block at the given position.
   *
   * @param stack Item stack that contains NBT tag with controller’s position.
   * @param world World to look for block.
   * @return The tile entity, null if none were found or tile entity is of the wrong type.
   */
  private static TileEntityLightOrbController getControllerTileEntity(ItemStack stack, World world) {
    NBTTagCompound tag = stack.getTagCompound();
    if (tag != null) {
      return Utils.getTileEntity(TileEntityLightOrbController.class, world,
          NBTUtil.getPosFromTag(tag.getCompoundTag(CONTROLLER_POS_TAG_KEY))).orElse(null);
    }
    return null;
  }
}

package net.darmo_creations.naissancee.items;

import net.darmo_creations.naissancee.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * Item used to measure lengths, areas and volumes.
 * <p>
 * Usage:
 * <li>Right-click on block to select first position.
 * <li>Right-click on another block to select second position.
 * The size, areas and volume the selected 3D rectangle will appear in the chat.
 */
public class ItemRuler extends Item {
  private static final String POS_TAG_KEY = "Pos";

  public ItemRuler() {
    this.setMaxStackSize(1);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    ItemStack heldItem = player.getHeldItem(hand);
    RulerData data = RulerData.fromTag(heldItem.getTagCompound());

    if (data.position == null) {
      data.position = pos;
      Utils.sendMessage(world, player, new TextComponentString(
          "Selected first position: " + Utils.blockPosToString(pos))
          .setStyle(new Style().setColor(TextFormatting.AQUA)));
    } else {
      Utils.sendMessage(world, player, new TextComponentString(
          "Selected second position: " + Utils.blockPosToString(pos))
          .setStyle(new Style().setColor(TextFormatting.DARK_AQUA)));

      Vec3i lengths = Utils.getLengths(data.position, pos);
      int lengthX = lengths.getX();
      int lengthY = lengths.getY();
      int lengthZ = lengths.getZ();
      Utils.sendMessage(world, player, new TextComponentString(
          String.format("Size (XYZ): %d x %d x %d", lengthX, lengthY, lengthZ))
          .setStyle(new Style().setColor(TextFormatting.GREEN)));

      // Do not display any area if at least two dimensions have a length of 1 (single line of blocks selected)
      if (lengthX + lengthY != 2 && lengthX + lengthZ != 2 && lengthY + lengthZ != 2) {
        Vec3i areas = Utils.getAreas(data.position, pos);
        int areaX = areas.getX();
        int areaY = areas.getY();
        int areaZ = areas.getZ();
        // Only display relevent area if player selected a 1-block-thick volume
        if (lengthX == 1 || lengthY == 1 || lengthZ == 1) {
          int area;
          if (lengthX == 1) {
            area = areaX;
          } else if (lengthZ == 1) {
            area = areaZ;
          } else {
            area = areaY;
          }
          Utils.sendMessage(world, player, new TextComponentString(
              String.format("Area: %d", area))
              .setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
        } else {
          Utils.sendMessage(world, player, new TextComponentString(
              String.format("Areas (XYZ): %d, %d, %d", areaX, areaY, areaZ))
              .setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
        }
      }

      int volume = Utils.getVolume(data.position, pos);
      Utils.sendMessage(world, player, new TextComponentString(
          String.format("Volume: %d", volume))
          .setStyle(new Style().setColor(TextFormatting.GOLD)));

      data.position = null;
    }

    heldItem.setTagCompound(data.toTag());
    return EnumActionResult.SUCCESS;
  }

  /**
   * Class holding data for the ruler that can serialize/deserialize NBT tags.
   */
  private static class RulerData {
    /**
     * Create a data instance from the given NBT tags.
     * If tag is null, an empty instance is returned.
     *
     * @param data NBT tag to deserialize.
     * @return The RulerData object.
     */
    static RulerData fromTag(NBTTagCompound data) {
      if (data != null) {
        NBTTagCompound tag = data.getCompoundTag(POS_TAG_KEY);
        return new RulerData(!tag.hasNoTags() ? NBTUtil.getPosFromTag(tag) : null);
      } else {
        return new RulerData();
      }
    }

    BlockPos position;

    /**
     * Create an empty object.
     */
    RulerData() {
      this(null);
    }

    /**
     * Create an object for the given positions.
     */
    RulerData(BlockPos position) {
      this.position = position;
    }

    /**
     * Convert this data object to NBT tags.
     */
    NBTTagCompound toTag() {
      NBTTagCompound root = new NBTTagCompound();

      if (this.position != null) {
        root.setTag(POS_TAG_KEY, NBTUtil.createPosTag(this.position));
      }

      return root;
    }
  }
}

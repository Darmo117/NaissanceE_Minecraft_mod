package net.darmo_creations.naissancee.items;

import net.darmo_creations.naissancee.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Item used to fill areas with blocks.
 * <p>
 * Usage:
 * <li>Right-click on block to select first position.
 * <li>Right-click on another block to select second position.
 * <li>Sneak-right-click on block to select filler block. If no block is targetted, air will be selected.
 */
public class ItemCreativeWand extends Item {
  private static final String POS1_TAG_KEY = "Pos1";
  private static final String POS2_TAG_KEY = "Pos2";
  private static final String STATE_TAG_KEY = "BlockState";

  // Maximum number of blocks that can be filled at the same time
  private static final int VOLUME_LIMIT = 32768;

  public ItemCreativeWand() {
    this.setMaxStackSize(1);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack heldItem = player.getHeldItem(hand);
    WandData data = WandData.fromTag(heldItem.getTagCompound());

    EnumActionResult result;
    if (player.isSneaking()) {
      setBlockState(Blocks.AIR.getDefaultState(), data, world, player);
      heldItem.setTagCompound(data.toTag());
      result = EnumActionResult.SUCCESS;
    } else {
      if (data.isReady()) {
        IBlockState state = data.blockState;
        Pair<BlockPos, BlockPos> positions = Utils.normalizePositions(data.firstPosition, data.secondPosition);
        BlockPos posMin = positions.getLeft();
        BlockPos posMax = positions.getRight();
        int volume = (posMax.getX() - posMin.getX() + 1)
            * (posMax.getY() - posMin.getY() + 1)
            * (posMax.getZ() - posMin.getZ() + 1);

        if (volume > VOLUME_LIMIT) {
          String message = String.format("Too many blocks in the specified volume (%d > %d)!", volume, VOLUME_LIMIT);
          Utils.sendMessage(world, player, new TextComponentString(message)
              .setStyle(new Style().setColor(TextFormatting.RED)));
          result = EnumActionResult.FAIL;
        } else {
          List<BlockPos> list = new ArrayList<>();

          for (int x = posMin.getX(); x <= posMax.getX(); x++) {
            for (int y = posMin.getY(); y <= posMax.getY(); y++) {
              for (int z = posMin.getZ(); z <= posMax.getZ(); z++) {
                BlockPos p = new BlockPos(x, y, z);
                world.setBlockState(p, state, 2);
                list.add(p);
              }
            }
          }

          for (BlockPos blockpos5 : list) {
            Block block2 = world.getBlockState(blockpos5).getBlock();
            world.notifyNeighborsRespectDebug(blockpos5, block2, false);
          }

          heldItem.setTagCompound(data.toTag());
          String message = String.format(
              "Filled area with %d %s block%s.",
              volume,
              Utils.getBlockID(state.getBlock()),
              volume > 1 ? "s" : ""
          );
          Utils.sendMessage(world, player, new TextComponentString(message)
              .setStyle(new Style().setColor(TextFormatting.DARK_GREEN)));
          result = EnumActionResult.SUCCESS;
        }
      } else {
        Utils.sendMessage(world, player, new TextComponentString("Cannot fill area!")
            .setStyle(new Style().setColor(TextFormatting.RED)));
        result = EnumActionResult.FAIL;
      }
    }

    return new ActionResult<>(result, heldItem);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    ItemStack heldItem = player.getHeldItem(hand);
    WandData data = WandData.fromTag(heldItem.getTagCompound());

    if (player.isSneaking()) {
      setBlockState(world.getBlockState(pos), data, world, player);
    } else {
      if (data.firstPosition == null || data.secondPosition != null) {
        data.firstPosition = pos;
        data.secondPosition = null;
        Utils.sendMessage(world, player, new TextComponentString(
            "Selected first position: " + Utils.blockPosToString(pos))
            .setStyle(new Style().setColor(TextFormatting.AQUA)));
      } else {
        data.secondPosition = pos;
        Utils.sendMessage(world, player, new TextComponentString(
            "Selected second position: " + Utils.blockPosToString(pos))
            .setStyle(new Style().setColor(TextFormatting.DARK_AQUA)));
      }
    }

    heldItem.setTagCompound(data.toTag());
    return EnumActionResult.SUCCESS;
  }

  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
    WandData data = WandData.fromTag(stack.getTagCompound());
    tooltip.add(TextFormatting.AQUA + "First position: "
        + (data.firstPosition != null ? Utils.blockPosToString(data.firstPosition) : "-"));
    tooltip.add(TextFormatting.DARK_AQUA + "Second position: "
        + (data.secondPosition != null ? Utils.blockPosToString(data.secondPosition) : "-"));
    tooltip.add(TextFormatting.BLUE + "Block state: "
        + (data.blockState != null ? Utils.blockstateToString(data.blockState) : "-"));
  }

  /**
   * Set block state of tool then send a confirmation message to player.
   *
   * @param blockState Block state to use.
   * @param data       Wand data.
   * @param world      World the player is in.
   * @param player     Player to send chat message to.
   */
  private static void setBlockState(IBlockState blockState, WandData data, World world, EntityPlayer player) {
    data.blockState = blockState;
    Utils.sendMessage(world, player, new TextComponentString(
        "Selected block state: " + Utils.blockstateToString(blockState))
        .setStyle(new Style().setColor(TextFormatting.BLUE)));
  }

  /**
   * Class holding data for the wand that can serialize/deserialize NBT tags.
   */
  private static class WandData {
    /**
     * Create a data instance from the given NBT tags.
     * If tag is null, an empty instance is returned.
     *
     * @param data NBT tag to deserialize.
     * @return The WandData object.
     */
    static WandData fromTag(NBTTagCompound data) {
      if (data != null) {
        NBTTagCompound tag1 = data.getCompoundTag(POS1_TAG_KEY);
        NBTTagCompound tag2 = data.getCompoundTag(POS2_TAG_KEY);
        NBTTagCompound tagState = data.getCompoundTag(STATE_TAG_KEY);
        BlockPos pos1 = !tag1.hasNoTags() ? NBTUtil.getPosFromTag(tag1) : null;
        BlockPos pos2 = !tag2.hasNoTags() ? NBTUtil.getPosFromTag(tag2) : null;
        IBlockState state = !tagState.hasNoTags() ? NBTUtil.readBlockState(tagState) : null;
        return new WandData(pos1, pos2, state);
      } else {
        return new WandData();
      }
    }

    BlockPos firstPosition;
    BlockPos secondPosition;
    IBlockState blockState;

    /**
     * Create an empty object.
     */
    WandData() {
      this(null, null, null);
    }

    /**
     * Create an object for the given positions and block state.
     */
    WandData(BlockPos firstPosition, BlockPos secondPosition, IBlockState blockState) {
      this.firstPosition = firstPosition;
      this.secondPosition = secondPosition;
      this.blockState = blockState;
    }

    /**
     * Data object is considered ready when both positions and blockstate are set.
     */
    boolean isReady() {
      return this.firstPosition != null && this.secondPosition != null && this.blockState != null;
    }

    /**
     * Convert this data object to NBT tags.
     */
    NBTTagCompound toTag() {
      NBTTagCompound root = new NBTTagCompound();

      if (this.firstPosition != null) {
        root.setTag(POS1_TAG_KEY, NBTUtil.createPosTag(this.firstPosition));
      }

      if (this.secondPosition != null) {
        root.setTag(POS2_TAG_KEY, NBTUtil.createPosTag(this.secondPosition));
      }

      if (this.blockState != null) {
        NBTTagCompound tag = new NBTTagCompound();
        NBTUtil.writeBlockState(tag, this.blockState);
        root.setTag(STATE_TAG_KEY, tag);
      }

      return root;
    }
  }
}

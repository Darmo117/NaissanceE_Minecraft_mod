package net.darmo_creations.naissancee.items;

import net.darmo_creations.naissancee.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemCreativeWand extends Item {
  public static final String POS1_TAG_KEY = "Pos1";
  public static final String POS2_TAG_KEY = "Pos2";
  public static final String STATE_TAG_KEY = "BlockState";

  public static final int AREA_SIZE_LIMIT = 32768;

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
        BlockPos pos1 = data.getFirstPosition();
        BlockPos pos2 = data.getSecondPosition();
        IBlockState state = data.getBlockState();
        BlockPos posMin = new BlockPos(
            Math.min(pos1.getX(), pos2.getX()),
            Math.min(pos1.getY(), pos2.getY()),
            Math.min(pos1.getZ(), pos2.getZ())
        );
        BlockPos posMax = new BlockPos(
            Math.max(pos1.getX(), pos2.getX()),
            Math.max(pos1.getY(), pos2.getY()),
            Math.max(pos1.getZ(), pos2.getZ())
        );
        int areaSize = (posMax.getX() - posMin.getX() + 1)
            * (posMax.getY() - posMin.getY() + 1)
            * (posMax.getZ() - posMin.getZ() + 1);

        if (areaSize > AREA_SIZE_LIMIT) {
          String message = String.format(
              "Too many blocks in the specified area (%d > %d)!",
              areaSize,
              AREA_SIZE_LIMIT
          );
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

          data.setFirstPosition(null);
          data.setSecondPosition(null);
          heldItem.setTagCompound(data.toTag());
          String message = String.format(
              "Filled area with %d %s block%s.",
              areaSize,
              getBlockID(state.getBlock()),
              areaSize > 1 ? "s" : ""
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
      if (data.getFirstPosition() == null || data.getSecondPosition() != null) {
        data.setFirstPosition(pos);
        data.setSecondPosition(null);
        Utils.sendMessage(world, player, new TextComponentString(String.format("Selected first position: %s", blockPosToString(pos)))
            .setStyle(new Style().setColor(TextFormatting.AQUA)));
      } else {
        data.setSecondPosition(pos);
        Utils.sendMessage(world, player, new TextComponentString(String.format("Selected second position: %s", blockPosToString(pos)))
            .setStyle(new Style().setColor(TextFormatting.DARK_AQUA)));
      }
    }

    heldItem.setTagCompound(data.toTag());
    return EnumActionResult.SUCCESS;
  }

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
    super.onUpdate(stack, world, entity, itemSlot, isSelected);
    // TODO encadrer blocs et zone sélectionnées
  }

  @Override
  public String getHighlightTip(ItemStack item, String displayName) {
    // TODO afficher infos dans popup
    return super.getHighlightTip(item, displayName);
  }

  private static String blockPosToString(BlockPos pos) {
    return String.format("%d %d %d", pos.getX(), pos.getY(), pos.getZ());
  }

  private static String getBlockID(Block block) {
    //noinspection ConstantConditions
    return GameRegistry.findRegistry(Block.class).getKey(block).toString();
  }

  private static void setBlockState(IBlockState blockState, WandData data, World world, EntityPlayer player) {
    data.setBlockState(blockState);
    String message = getBlockID(blockState.getBlock());
    Map<IProperty<?>, Comparable<?>> properties = blockState.getProperties();
    if (!properties.isEmpty()) {
      message += " " + properties.entrySet().stream()
          .collect(Collectors.toMap(e -> e.getKey().getName(), Map.Entry::getValue));
    }
    Utils.sendMessage(world, player, new TextComponentString(String.format("Selected block state: %s", message))
        .setStyle(new Style().setColor(TextFormatting.BLUE)));
  }

  private static class WandData {
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

    private BlockPos firstPosition;
    private BlockPos secondPosition;
    private IBlockState blockState;

    WandData() {
      this(null, null, null);
    }

    WandData(BlockPos firstPosition, BlockPos secondPosition, IBlockState blockState) {
      this.firstPosition = firstPosition;
      this.secondPosition = secondPosition;
      this.blockState = blockState;
    }

    boolean isReady() {
      return this.getFirstPosition() != null && this.getSecondPosition() != null && this.getBlockState() != null;
    }

    BlockPos getFirstPosition() {
      return this.firstPosition;
    }

    void setFirstPosition(BlockPos pos) {
      this.firstPosition = pos;
    }

    BlockPos getSecondPosition() {
      return this.secondPosition;
    }

    void setSecondPosition(BlockPos pos) {
      this.secondPosition = pos;
    }

    IBlockState getBlockState() {
      return this.blockState;
    }

    void setBlockState(IBlockState blockState) {
      this.blockState = blockState;
    }

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

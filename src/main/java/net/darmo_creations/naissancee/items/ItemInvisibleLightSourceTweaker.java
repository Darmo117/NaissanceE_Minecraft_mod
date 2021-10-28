package net.darmo_creations.naissancee.items;

import net.darmo_creations.naissancee.Utils;
import net.darmo_creations.naissancee.blocks.BlockInvisibleLightSource;
import net.darmo_creations.naissancee.blocks.ModBlocks;
import net.darmo_creations.naissancee.tile_entities.TileEntityInvisibleLightSource;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

/**
 * Item used to edit invisible light sources.
 * This item is not stackable.
 * <p>
 * Usage:
 * <li>Right-click while sneaking: toggle tool mode between set block and set light level.
 * <li>Right-click on light source: cycle through mode or light levels depending on tool’s mode;
 * sneak to cycle the other way around.
 */
public class ItemInvisibleLightSourceTweaker extends Item {
  private static final String MODE_ID_TAG_KEY = "ModeID";

  public ItemInvisibleLightSourceTweaker() {
    this.setMaxStackSize(1);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack heldItem = player.getHeldItem(hand);
    EnumMode mode = getMode(player.getHeldItem(hand));

    EnumActionResult result;
    if (player.isSneaking()) {
      NBTTagCompound tag = new NBTTagCompound();
      int newModeID = (mode.ordinal() + 1) % EnumMode.values().length;
      tag.setInteger(MODE_ID_TAG_KEY, newModeID);
      heldItem.setTagCompound(tag);
      Utils.sendMessage(world, player, new TextComponentString("New Mode: " + EnumMode.values()[newModeID].label));
      result = EnumActionResult.PASS;
    } else {
      result = EnumActionResult.FAIL;
    }

    return new ActionResult<>(result, heldItem);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    EnumMode mode = getMode(player.getHeldItem(hand));
    Optional<TileEntityInvisibleLightSource> te = Utils.getTileEntity(TileEntityInvisibleLightSource.class, world, pos);

    EnumActionResult result = EnumActionResult.FAIL;
    if (te.isPresent()) {
      switch (mode) {
        case SET_LIGHT_LEVEL:
          if (te.get().getMode() == TileEntityInvisibleLightSource.EnumMode.EDIT) {
            IBlockState state = world.getBlockState(pos);
            int lightLevel = state.getValue(BlockInvisibleLightSource.LIGHT_LEVEL);
            if (player.isSneaking()) {
              lightLevel = Utils.trueModulo(lightLevel - 1, 16);
            } else {
              lightLevel = Utils.trueModulo(lightLevel + 1, 16);
            }
            world.setBlockState(pos, state.withProperty(BlockInvisibleLightSource.LIGHT_LEVEL, lightLevel));
            result = EnumActionResult.SUCCESS;
          }
          break;

        case SET_BLOCK_MODE:
          int l = TileEntityInvisibleLightSource.EnumMode.values().length;
          int modeID = te.get().getMode().ordinal();
          if (player.isSneaking()) {
            modeID = Utils.trueModulo(modeID - 1, l);
          } else {
            modeID = Utils.trueModulo(modeID + 1, l);
          }
          te.get().setMode(TileEntityInvisibleLightSource.EnumMode.values()[modeID]);
          result = EnumActionResult.SUCCESS;
          break;
      }
    }

    if (result == EnumActionResult.SUCCESS) {
      world.notifyNeighborsOfStateChange(pos, ModBlocks.INVISIBLE_LIGHT_SOURCE, true);
      this.playClickSound(world, pos);
    }

    return result;
  }

  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
    tooltip.add("Mode: " + getMode(stack).label);
  }

  private static EnumMode getMode(ItemStack stack) {
    NBTTagCompound tag = stack.getTagCompound();
    return tag != null ? EnumMode.values()[tag.getInteger(MODE_ID_TAG_KEY)] : EnumMode.SET_LIGHT_LEVEL;
  }

  /**
   * Plays a "click" sound at the given position.
   */
  private void playClickSound(World world, BlockPos pos) {
    world.playSound(null, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
  }

  private enum EnumMode {
    SET_LIGHT_LEVEL("Set Light Level"),
    SET_BLOCK_MODE("Set Mode");

    public final String label;

    EnumMode(String label) {
      this.label = label;
    }
  }
}

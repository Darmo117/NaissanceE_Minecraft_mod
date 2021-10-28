package net.darmo_creations.naissancee;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class defines various utility functions.
 */
public final class Utils {
  /**
   * Returns the tile entity of the given class at the given position.
   *
   * @param tileEntityClass Tile entity’s class.
   * @param world           The world.
   * @param pos             Tile entity’s position.
   * @param <T>             Tile entity’s type.
   * @return The tile entity if found, an empty optional otherwise.
   */
  public static <T extends TileEntity> Optional<T> getTileEntity(Class<T> tileEntityClass, World world, BlockPos pos) {
    TileEntity te = world.getTileEntity(pos);
    if (tileEntityClass.isInstance(te)) {
      return Optional.of(tileEntityClass.cast(te));
    }
    return Optional.empty();
  }

  /**
   * Return registry ID of a block.
   */
  public static String getBlockID(Block block) {
    //noinspection ConstantConditions
    return GameRegistry.findRegistry(Block.class).getKey(block).toString();
  }

  /**
   * Convert block position to string.
   */
  public static String blockPosToString(BlockPos pos) {
    return String.format("%d %d %d", pos.getX(), pos.getY(), pos.getZ());
  }

  /**
   * Serialize a block state to a string.
   *
   * @param blockState Block state to serialize.
   * @return Serialized string.
   */
  public static String blockstateToString(IBlockState blockState) {
    String message = getBlockID(blockState.getBlock());
    Map<IProperty<?>, Comparable<?>> properties = blockState.getProperties();
    if (!properties.isEmpty()) {
      message += " " + properties.entrySet().stream()
          .collect(Collectors.toMap(e -> e.getKey().getName(), Map.Entry::getValue));
    }
    return message;
  }

  /**
   * Performs a true modulo operation using the mathematical definition of "a mod b".
   *
   * @param a Value to get the modulo of.
   * @param b The divisor.
   * @return a mod b
   */
  public static int trueModulo(int a, int b) {
    return ((a % b) + b) % b;
  }

  /**
   * Sends a chat message to a player. Does nothing if the world is remote (i.e. client-side).
   *
   * @param world  The world the player is in.
   * @param player The player.
   * @param text   Message’s text.
   */
  public static void sendMessage(World world, EntityPlayer player, ITextComponent text) {
    if (!world.isRemote) {
      player.sendMessage(text);
    }
  }

  private Utils() {
  }
}

package net.darmo_creations.naissancee;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.Optional;

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

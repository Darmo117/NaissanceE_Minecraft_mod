package net.darmo_creations.naissancee;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class defines various utility functions.
 */
public final class Utils {
  public static final int WHITE = 0xffffff;
  public static final int GRAY = 0xa0a0a0;
  public static final int RED = 0xff0000;

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

  /**
   * Get the blocks length along each axis of the volume defined by the given positions.
   *
   * @return A {@link Vec3i} object. Each axis holds the number of blocks along itself.
   */
  public static Vec3i getLengths(BlockPos pos1, BlockPos pos2) {
    Pair<BlockPos, BlockPos> positions = normalizePositions(pos1, pos2);
    BlockPos posMin = positions.getLeft();
    BlockPos posMax = positions.getRight();

    return new Vec3i(
        posMax.getX() - posMin.getX() + 1,
        posMax.getY() - posMin.getY() + 1,
        posMax.getZ() - posMin.getZ() + 1
    );
  }

  /**
   * Get the blocks area of each face of the volume defined by the given positions.
   *
   * @return A {@link Vec3i} object. Each axis holds the area of the face perpendicular to itself.
   */
  public static Vec3i getAreas(BlockPos pos1, BlockPos pos2) {
    Vec3i size = getLengths(pos1, pos2);

    return new Vec3i(
        size.getZ() * size.getY(),
        size.getX() * size.getZ(),
        size.getX() * size.getY()
    );
  }

  /**
   * Get the total number of blocks inside the volume defined by the given positions.
   */
  public static int getVolume(BlockPos pos1, BlockPos pos2) {
    Vec3i size = getLengths(pos1, pos2);
    return size.getX() * size.getY() * size.getZ();
  }

  /**
   * Return the highest and lowest block coordinates for the volume defined by the given two positions.
   *
   * @param pos1 First position.
   * @param pos2 Second position.
   * @return A pair with the lowest and highest positions.
   */
  public static Pair<BlockPos, BlockPos> normalizePositions(BlockPos pos1, BlockPos pos2) {
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
    return new ImmutablePair<>(posMin, posMax);
  }

  private Utils() {
  }
}

package net.darmo_creations.naissancee.entities;

import net.minecraft.util.math.BlockPos;

/**
 * A path checkpoint represents a block position through which a light orb must pass.
 * Several checkpoints form a path a light orb must follow.
 */
public interface IPathCheckpoint {
  /**
   * The block position of this checkpoint.
   */
  BlockPos getPos();

  /**
   * Whether the light orb should stop at this checkpoint.
   */
  boolean isStop();
}

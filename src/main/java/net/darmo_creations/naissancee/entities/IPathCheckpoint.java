package net.darmo_creations.naissancee.entities;

import net.minecraft.util.math.BlockPos;

public interface IPathCheckpoint {
  BlockPos getPos();

  boolean isStop();
}

package net.darmo_creations.naissancee.blocks;

public interface IModBlock {
  default boolean hasGeneratedItemBlock() {
    return true;
  }
}

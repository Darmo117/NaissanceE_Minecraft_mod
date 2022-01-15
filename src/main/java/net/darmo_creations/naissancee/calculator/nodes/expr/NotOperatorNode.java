package net.darmo_creations.naissancee.calculator.nodes.expr;

import net.minecraft.nbt.NBTTagCompound;

/**
 * A {@link Node} representing the logical "not" operator. Returns 1 if the number is 0, and 0 if the number is not 0.
 */
public class NotOperatorNode extends UnaryOperatorNode {
  public static final int ID = 301;

  /**
   * Create a logical "not" operator.
   *
   * @param operand The expression to compute the negation of.
   */
  public NotOperatorNode(final Node operand) {
    super("!", operand);
  }

  /**
   * Create a logical "not" operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public NotOperatorNode(final NBTTagCompound tag) {
    super(tag);
  }

  @Override
  protected double evaluateImpl(final double value) {
    return value == 0 ? 1 : 0;
  }

  @Override
  public int getID() {
    return ID;
  }
}

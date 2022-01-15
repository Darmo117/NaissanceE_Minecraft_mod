package net.darmo_creations.naissancee.calculator.nodes.expr;

import net.minecraft.nbt.NBTTagCompound;

/**
 * A {@link Node} representing the addition operator (a + b).
 */
public class AdditionOperatorNode extends BiOperatorNode {
  public static final int ID = 400;

  /**
   * Create an addition operator: left + right.
   *
   * @param left  The left operand.
   * @param right The right operand.
   */
  public AdditionOperatorNode(final Node left, final Node right) {
    super("+", left, right);
  }

  /**
   * Create an addition operator from an NBT tag.
   *
   * @param tag The tag to deserialize.
   */
  public AdditionOperatorNode(final NBTTagCompound tag) {
    super(tag);
  }

  @Override
  protected double evaluateImpl(final double left, final double right) {
    return left + right;
  }

  @Override
  public int getID() {
    return ID;
  }
}

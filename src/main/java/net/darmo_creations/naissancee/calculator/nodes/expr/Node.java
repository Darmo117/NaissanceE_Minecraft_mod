package net.darmo_creations.naissancee.calculator.nodes.expr;

import net.darmo_creations.naissancee.calculator.Scope;
import net.darmo_creations.naissancee.calculator.exceptions.EvaluationException;
import net.darmo_creations.naissancee.calculator.exceptions.UndefinedVariableException;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A node is the base component of an expression tree.
 */
public abstract class Node {
  public static final String ID_KEY = "NodeID";

  /**
   * Evaluate this node.
   *
   * @param scope The scope to use.
   * @return The value of this node.
   * @throws EvaluationException If an error occured during evaluation.
   * @throws ArithmeticException If a math error occured.
   */
  public abstract double evaluate(final Scope scope) throws UndefinedVariableException, ArithmeticException;

  /**
   * Serialize this node into an NBT tag.
   *
   * @return The serialized data.
   */
  public NBTTagCompound writeToNBT() {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger(ID_KEY, this.getID());
    return tag;
  }

  /**
   * Return the type ID of this Node.
   */
  public abstract int getID();

  @Override
  public abstract boolean equals(Object o);

  @Override
  public abstract int hashCode();

  @Override
  public abstract String toString();
}

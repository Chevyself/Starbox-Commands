package com.starfishst.core.arguments;

import com.starfishst.core.arguments.type.ISimpleArgument;
import org.jetbrains.annotations.NotNull;

/** An extra argument will be in the command method but it is not needed in the command string */
public class ExtraArgument<O> implements ISimpleArgument<O> {

  /** Get the class that represents the argument */
  @NotNull private final Class<O> clazz;

  /**
   * Get a new instance of {@link ExtraArgument}
   *
   * @param clazz the class of the argument
   */
  public ExtraArgument(@NotNull Class<O> clazz) {
    this.clazz = clazz;
  }

  @NotNull
  @Override
  public Class<O> getClazz() {
    return this.clazz;
  }

  @Override
  public String toString() {
    return "ExtraArgument{" + "clazz=" + this.clazz + '}';
  }
}

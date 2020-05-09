package com.starfishst.core.arguments.type;

import org.jetbrains.annotations.NotNull;

/** A simple argument only requires the class of the argument */
public interface ISimpleArgument<O> {

  /**
   * Get the class of the argument
   *
   * @return the class of the argument
   */
  @NotNull
  Class<O> getClazz();
}

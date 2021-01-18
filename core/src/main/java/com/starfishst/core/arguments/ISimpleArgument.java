package com.starfishst.core.arguments;

import lombok.NonNull;

/** A simple argument only requires the class of the argument */
public interface ISimpleArgument<O> {

  /**
   * Get the class of the argument
   *
   * @return the class of the argument
   */
  @NonNull
  Class<O> getClazz();
}

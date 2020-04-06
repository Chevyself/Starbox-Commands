package com.starfishst.core.providers.type;

import org.jetbrains.annotations.NotNull;

/**
 * A simple provider only requires the class of the object
 */
public interface ISimpleArgumentProvider<O> {

  /**
   * Get the class to provide
   *
   * @return the class to provide
   */
  @NotNull
  Class<O> getClazz();
}

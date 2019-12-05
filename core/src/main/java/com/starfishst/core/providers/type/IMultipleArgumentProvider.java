package com.starfishst.core.providers.type;

import org.jetbrains.annotations.NotNull;

/**
 * Gets the object using multiple strings from the command
 *
 * @param <O> the class to provide as argument
 */
public interface IMultipleArgumentProvider<O> extends ISimpleArgumentProvider {

  /**
   * Get the object of the provider
   *
   * @param strings the strings to get the object from
   * @return the object
   */
  @NotNull
  O fromStrings(@NotNull String[] strings);
}

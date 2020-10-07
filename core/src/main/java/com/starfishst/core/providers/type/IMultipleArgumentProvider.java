package com.starfishst.core.providers.type;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import org.jetbrains.annotations.NotNull;

/**
 * Gets the object using multiple strings from the command
 *
 * @param <O> the class to provide as argument
 */
public interface IMultipleArgumentProvider<O, T extends ICommandContext>
    extends IContextualProvider<O, T> {

  /**
   * Get the object of the provider
   *
   * @param strings the strings to get the object from
   * @param context the command context
   * @return the object
   * @throws ArgumentProviderException if the argument could not be provided
   */
  @NotNull
  O fromStrings(@NotNull String[] strings, @NotNull T context) throws ArgumentProviderException;
}

package me.googas.commands.providers.type;

import lombok.NonNull;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;

/**
 * Gets the object using multiple strings from the command
 *
 * @param <O> the class to provide as argument
 */
public interface EasyMultipleArgumentProvider<O, T extends EasyCommandContext>
    extends EasyContextualProvider<O, T> {

  /**
   * Get the object of the provider
   *
   * @param strings the strings to get the object from
   * @param context the command context
   * @return the object
   * @throws ArgumentProviderException if the argument could not be provided
   */
  @NonNull
  O fromStrings(@NonNull String[] strings, @NonNull T context) throws ArgumentProviderException;
}

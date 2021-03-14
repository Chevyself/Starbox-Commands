package me.googas.commands.providers.type;

import lombok.NonNull;
import me.googas.commands.ICommand;
import me.googas.commands.context.ICommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;

/**
 * A provider gives to the {@link ICommand} the object requested by it
 *
 * @param <O> the class to provide as argument
 */
public interface IArgumentProvider<O, T extends ICommandContext> extends IContextualProvider<O, T> {

  /**
   * Get the class instance for the {@link ICommand}
   *
   * @param string the string to get the object from
   * @param context the context used in the command
   * @return a new instance of {@link O}
   * @throws ArgumentProviderException when the object could not be obtained
   */
  @NonNull
  O fromString(@NonNull String string, @NonNull T context) throws ArgumentProviderException;
}

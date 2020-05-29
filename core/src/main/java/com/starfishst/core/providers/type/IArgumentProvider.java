package com.starfishst.core.providers.type;

import com.starfishst.core.ICommand;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import org.jetbrains.annotations.NotNull;

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
  @NotNull
  O fromString(@NotNull String string, @NotNull T context) throws ArgumentProviderException;
}

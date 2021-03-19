package me.googas.commands.providers.type;

import lombok.NonNull;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;

/**
 * This provider is used for {@link me.googas.commands.arguments.SingleArgument} as it only requires
 * one input being the {@link String} and as it is a {@link EasyContextualProvider} requires the
 * {@link EasyCommandContext}
 *
 * @param <O> the type of the object to provide
 * @param <T> the type of context that this requires to provide the object
 */
public interface EasyArgumentProvider<O, T extends EasyCommandContext>
    extends EasyContextualProvider<O, T> {

  /**
   * Get the instance of the {@link #getClazz()} to provide
   *
   * @param string the string to get the object from
   * @param context the context of the command execution
   * @return a new instance of {@link O}
   * @throws ArgumentProviderException when the object could not be obtained. For example getting a
   *     number from "A" is not possible and will cause a {@link NumberFormatException} which can be
   *     wrapped in a {@link ArgumentProviderException} to not {@link Throwable#printStackTrace()}
   *     but to tell the user to use a correct input
   */
  @NonNull
  O fromString(@NonNull String string, @NonNull T context) throws ArgumentProviderException;
}

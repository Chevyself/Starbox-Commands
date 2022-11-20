package chevyself.github.commands.providers.type;

import chevyself.github.commands.arguments.MultipleArgument;
import chevyself.github.commands.context.StarboxCommandContext;
import chevyself.github.commands.exceptions.ArgumentProviderException;
import lombok.NonNull;

/**
 * This provider is used for {@link MultipleArgument} as it requires joined strings and the context
 * of the command execution
 *
 * @param <O> the type of object to provide
 * @param <T> the type of context that this requires to provide the object
 */
@Deprecated
public interface StarboxMultipleArgumentProvider<O, T extends StarboxCommandContext>
    extends StarboxContextualProvider<O, T> {

  /**
   * Get the instance of the {@link #getClazz()} to provide.
   *
   * @param strings the strings to get the object from
   * @param context the context of the command execution
   * @return a new instance of {@link O}
   * @throws ArgumentProviderException when the object could not be obtained. For example getting a
   *     number from "A" is not possible and will cause a {@link NumberFormatException} which can be
   *     wrapped in a {@link ArgumentProviderException} to not {@link Throwable#printStackTrace()}
   *     but to tell the user to use a correct input
   */
  @NonNull
  O fromStrings(@NonNull String[] strings, @NonNull T context) throws ArgumentProviderException;
}

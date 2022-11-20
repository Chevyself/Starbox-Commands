package chevyself.github.commands.messages;

import chevyself.github.commands.arguments.Argument;
import chevyself.github.commands.arguments.MultipleArgument;
import chevyself.github.commands.context.StarboxCommandContext;
import java.time.Duration;
import lombok.NonNull;

/**
 * Provides messages for different instances of the manager.
 *
 * @param <T> the command context
 */
public interface StarboxMessagesProvider<T extends StarboxCommandContext> {

  /**
   * The message sent when a {@link String} is not valid as a {@link Long}.
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell the user that the input is wrong
   */
  @NonNull
  String invalidLong(@NonNull String string, @NonNull T context);

  /**
   * The message sent when a {@link String} is not valid as a {@link Integer}.
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidInteger(@NonNull String string, @NonNull T context);

  /**
   * The message sent when a {@link String} is not valid as a {@link Double}.
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidDouble(@NonNull String string, @NonNull T context);

  /**
   * The message sent when a {@link String} is not valid as a {@link Boolean}.
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidBoolean(@NonNull String string, @NonNull T context);

  /**
   * The message sent when a {@link String} is not valid as {@link java.time.Duration}.
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidDuration(@NonNull String string, @NonNull T context);

  /**
   * Get the message to send when there's a missing an {@link Argument}.
   *
   * @param name the name of the argument
   * @param description the description of the argument
   * @param position the position of the argument
   * @param context the context of the command
   * @return the message to tell that the command execution is missing an argument
   * @see Argument
   */
  @NonNull
  String missingArgument(
      @NonNull String name, @NonNull String description, int position, T context);

  /**
   * Get the message sent when a {@link MultipleArgument} min size is bigger than the context
   * strings.
   *
   * @param name the name of the argument
   * @param description the description of the argument
   * @param position the position of the argument
   * @param minSize the minimum size of the arguments
   * @param missing how many strings are missing
   * @param context the context of the command
   * @return the message to tell that the command execution is missing arguments
   * @see Argument
   * @see MultipleArgument
   */
  @NonNull
  @Deprecated
  String missingStrings(
      @NonNull String name,
      @NonNull String description,
      int position,
      int minSize,
      int missing,
      @NonNull T context);

  /**
   * Get the message sent when the user is still on cooldown.
   *
   * @param context the context of the command
   * @param timeLeft the time left for the user in the getMillis
   * @return the built string
   */
  @NonNull
  String cooldown(@NonNull T context, @NonNull Duration timeLeft);
}

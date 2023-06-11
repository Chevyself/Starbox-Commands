package com.github.chevyself.starbox.messages;

import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import java.time.Duration;
import lombok.NonNull;

/**
 * Provides messages for different instances of the manager.
 *
 * @param <C> the command context
 */
public interface StarboxMessagesProvider<C extends StarboxCommandContext<C, ?>> {

  /**
   * The message sent when a {@link String} is not valid as a {@link Long}.
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell the user that the input is wrong
   */
  @NonNull
  String invalidLong(@NonNull String string, @NonNull C context);

  /**
   * The message sent when a {@link String} is not valid as a {@link Integer}.
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidInteger(@NonNull String string, @NonNull C context);

  /**
   * The message sent when a {@link String} is not valid as a {@link Double}.
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidDouble(@NonNull String string, @NonNull C context);

  /**
   * The message sent when a {@link String} is not valid as a {@link Boolean}.
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidBoolean(@NonNull String string, @NonNull C context);

  /**
   * The message sent when a {@link String} is not valid as {@link java.time.Duration}.
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidDuration(@NonNull String string, @NonNull C context);

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
      @NonNull String name, @NonNull String description, int position, C context);

  /**
   * Get the message sent when the user is still on cooldown.
   *
   * @param context the context of the command
   * @param timeLeft the time left for the user in the getMillis
   * @return the built string
   */
  @NonNull
  String cooldown(@NonNull C context, @NonNull Duration timeLeft);

  /**
   * Generates the help message for a command.
   *
   * @see StarboxCommand#generateUsage(StarboxCommand)
   * @param command the command to generate the help for
   * @param context the context of the command
   * @return the help message
   */
  @NonNull
  String commandHelp(@NonNull StarboxCommand<C, ?> command, C context);
}

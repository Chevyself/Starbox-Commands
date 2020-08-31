package com.starfishst.core.messages;

import com.starfishst.core.context.ICommandContext;
import org.jetbrains.annotations.NotNull;

/**
 * Provides messages for different instances of the manager
 *
 * @param <T> the command context
 */
public interface IMessagesProvider<T extends ICommandContext> {

  /**
   * The message sent when a string is not valid as a long
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell the user that the input is wrong
   */
  @NotNull
  String invalidLong(@NotNull String string, @NotNull T context);

  /**
   * The message sent when a string is not valid as a integer
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NotNull
  String invalidInteger(@NotNull String string, @NotNull T context);

  /**
   * The message sent when a string is not valid as a double
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NotNull
  String invalidDouble(@NotNull String string, @NotNull T context);

  /**
   * The message sent when a string is not valid as a boolean
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NotNull
  String invalidBoolean(@NotNull String string, @NotNull T context);

  /**
   * The message sent when a string is not valid as Time
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NotNull
  String invalidTime(@NotNull String string, @NotNull T context);

  /**
   * Get the message to send when there's a missing a argument
   *
   * @param name the name of the argument
   * @param description the description of the argument
   * @param position the position of the argument
   * @param context the context of the command
   * @return The error when the message is missing arguments
   */
  @NotNull
  String missingArgument(
      @NotNull String name, @NotNull String description, int position, T context);

  /**
   * Get the message to send when a string is not a valid number
   *
   * @param string the string that is not a valid number
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NotNull
  String invalidNumber(@NotNull String string, @NotNull T context);

  /**
   * Get the message sent when the input string for a double is empty
   *
   * @param context the context of the command
   * @return the message to tell the user that a double cannot be empty
   */
  @NotNull
  String emptyDouble(@NotNull T context);
}

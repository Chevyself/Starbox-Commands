package me.googas.commands.jda.messages;

import lombok.NonNull;
import me.googas.commands.jda.ListenerOptions;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.result.ResultType;
import me.googas.commands.messages.StarboxMessagesProvider;

/** Provides messages to results. */
public interface MessagesProvider extends StarboxMessagesProvider<CommandContext> {

  /**
   * Get the message for when a command is not found.
   *
   * @param command is the input string that's not found as a command
   * @param context the context of the command
   * @return The message when a command is not found in {@link
   *     me.googas.commands.jda.CommandManager}
   */
  @NonNull
  String commandNotFound(@NonNull @Deprecated String command, @NonNull CommandContext context);

  /**
   * Get the footer for embeds.
   *
   * @param context the context of the command
   * @return The footer in case {@link ListenerOptions} embed messages is true
   */
  @NonNull
  String footer(CommandContext context);

  /**
   * Get the title for embeds.
   *
   * @param type the type of result
   * @param context the context of the command
   * @return the title to use for a result
   */
  @NonNull
  String getTitle(@NonNull ResultType type, CommandContext context);

  /**
   * Get the response when a result is not done as an embed.
   *
   * @param title the title of the response
   * @param message the message of the response
   * @param context the context of the command
   * @return the message when the result has a message
   */
  @NonNull
  String response(@NonNull String title, @NonNull String message, CommandContext context);

  /**
   * Get the message for when a user is not allowed to use a command.
   *
   * @param context the context of the command
   * @return the message when the sender does not have a permission
   */
  @NonNull
  String notAllowed(@NonNull CommandContext context);

  /**
   * Get the message for when a command may only be executed in a guild.
   *
   * @param context the context of the command
   * @return the message when the command has to be executed in a {@link
   *     net.dv8tion.jda.api.entities.Guild}
   */
  @NonNull
  String guildOnly(@NonNull CommandContext context);

  /**
   * Get the url to use as thumbnail.
   *
   * @param context the context of the command
   * @return the url to use as thumbnail
   */
  @NonNull
  String thumbnailUrl(CommandContext context);

  /**
   * Get the message sent when the user is still on cooldown.
   *
   * @param timeLeft the time left for the user in the getMillis
   * @param context the context of the command
   * @return the built string
   */
  @NonNull
  String cooldown(long timeLeft, CommandContext context);

  /**
   * The message sent when a string is not a valid user.
   *
   * @param string the string that is not valid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidUser(@NonNull String string, @NonNull CommandContext context);

  /**
   * The message sent when a string is not a valid user.
   *
   * @param string the string that is not valid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidMember(@NonNull String string, @NonNull CommandContext context);

  /**
   * The message sent when a string is not a valid role.
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidRole(@NonNull String string, @NonNull CommandContext context);

  /**
   * The message sent when a string is not a valid role.
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidTextChannel(String string, CommandContext context);

  /**
   * The message sent when a command was not executed from a {@link
   * net.dv8tion.jda.api.entities.Message}
   *
   * @param context the context of the command
   * @return the message to tell that the execution is wrong
   */
  String noMessage(@NonNull CommandContext context);
}

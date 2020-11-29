package com.starfishst.jda.messages;

import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.jda.CommandManager;
import com.starfishst.jda.ManagerOptions;
import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.result.ResultType;
import lombok.NonNull;
import me.googas.commons.time.Time;

/** Provides messages to results */
public interface MessagesProvider extends IMessagesProvider<CommandContext> {

  /**
   * @param command is the input string that's not found as a command
   * @param context the context of the command
   * @return The message when a command is not found in {@link CommandManager}
   */
  @NonNull
  String commandNotFound(@NonNull String command, @NonNull CommandContext context);

  /**
   * @param context the context of the command
   * @return The footer in case {@link ManagerOptions#isEmbedMessages()} is true
   */
  @NonNull
  String footer(CommandContext context);

  /**
   * @param type the type of result
   * @param context the context of the command
   * @return the title to use for a result
   */
  @NonNull
  String getTitle(@NonNull ResultType type, CommandContext context);

  /**
   * @param title the title of the response
   * @param message the message of the response
   * @param context the context of the command
   * @return the message when the result has a message
   */
  @NonNull
  String response(@NonNull String title, @NonNull String message, CommandContext context);

  /**
   * @param context the context of the command
   * @return the message when the sender does not have a permission
   */
  @NonNull
  String notAllowed(@NonNull CommandContext context);

  /**
   * @param context the context of the command
   * @return the message when the command has to be executed in a {@link
   *     net.dv8tion.jda.api.entities.Guild}
   */
  @NonNull
  String guildOnly(@NonNull CommandContext context);

  /**
   * Get the url to use as thumbnail
   *
   * @param context the context of the command
   * @return the url to use as thumbnail
   */
  @NonNull
  String thumbnailUrl(CommandContext context);

  /**
   * Get the message sent when the user is still on cooldown
   *
   * @param timeLeft the time left for the user
   * @param context the context of the command
   * @return the built string
   */
  @NonNull
  String cooldown(@NonNull Time timeLeft, CommandContext context);

  /**
   * The message sent when a string is not a valid user
   *
   * @param string the string that is not valid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidUser(@NonNull String string, @NonNull CommandContext context);

  /**
   * The message sent when a string is not a valid user
   *
   * @param string the string that is not valid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidMember(@NonNull String string, @NonNull CommandContext context);

  /**
   * The message sent when a string is not a valid role
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidRole(@NonNull String string, @NonNull CommandContext context);

  /**
   * The message sent when a string is not a valid role
   *
   * @param string the string that is invalid
   * @param context the context ofo the command
   * @return the message to tell that the input is wrong
   */
  @NonNull
  String invalidTextChannel(String string, CommandContext context);
}

package com.starfishst.commands.messages;

import com.starfishst.commands.CommandManager;
import com.starfishst.commands.ManagerOptions;
import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.result.ResultType;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.utils.time.Time;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

/** Provides messages to results */
public interface MessagesProvider extends IMessagesProvider<User, CommandContext> {

  /**
   * @param command is the input string that's not found as a command
   * @return The message when a command is not found in {@link CommandManager}
   */
  @NotNull
  String commandNotFound(@NotNull String command);

  /** @return The footer in case {@link ManagerOptions#isEmbedMessages()} is true */
  @NotNull
  String footer();

  /**
   * @param type the type of result
   * @return the title to use for a result
   */
  @NotNull
  String getTitle(@NotNull ResultType type);

  /**
   * @param title the title of the response
   * @param message the message of the response
   * @return the message when the result has a message
   */
  @NotNull
  String response(@NotNull String title, @NotNull String message);

  /** @return the message when the sender does not have a permission */
  @NotNull
  String notAllowed();

  /**
   * @return the message when the command has to be executed in a {@link
   *     net.dv8tion.jda.api.entities.Guild}
   */
  @NotNull
  String guildOnly();

  /**
   * Get the url to use as thumbnail
   *
   * @return the url to use as thumbnail
   */
  @NotNull
  String thumbnailUrl();

  /**
   * Get the message sent when the user is still on cooldown
   *
   * @param timeLeft the time left for the user
   * @return the built string
   */
  @NotNull
  String cooldown(Time timeLeft);

  /**
   * The message sent when a string is not a valid user
   *
   * @param string the string that is not valid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NotNull
  String invalidUser(@NotNull String string, @NotNull CommandContext context);

  /**
   * The message sent when a string is not a valid user
   *
   * @param string the string that is not valid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NotNull
  String invalidMember(@NotNull String string, @NotNull CommandContext context);

  /**
   * The message sent when a string is not a valid role
   *
   * @param string the string that is invalid
   * @param context the context of the command
   * @return the message to tell that the input is wrong
   */
  @NotNull
  String invalidRole(@NotNull String string, @NotNull CommandContext context);

  /**
   * The message sent when a string is not a valid role
   *
   * @param string the string that is invalid
   * @param context the context ofo the command
   * @return the message to tell that the input is wrong
   */
  @NotNull
  String invalidTextChannel(String string, CommandContext context);
}

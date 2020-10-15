package com.starfishst.bungee.messages;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.core.messages.IMessagesProvider;
import org.jetbrains.annotations.NotNull;

/** The messages provider for bungee */
public interface MessagesProvider extends IMessagesProvider<CommandContext> {

  /**
   * The message sent when the user that executed the command is not allowed to use it
   *
   * @param context the context of the command
   * @return the message to send
   */
  @NotNull
  String notAllowed(CommandContext context);

  /**
   * The message to send when the string does not match a proxied player
   *
   * @param string the string
   * @param context the context of the command
   * @return the message to send
   */
  @NotNull
  String invalidPlayer(@NotNull String string, @NotNull CommandContext context);

  /**
   * The message to send when a command that is required that the sender is a player happens to be
   * something else
   *
   * @param context the context of the command
   * @return the message
   */
  @NotNull
  String onlyPlayers(CommandContext context);
}
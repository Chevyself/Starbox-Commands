package com.starfishst.commands.bungee.messages;

import com.starfishst.commands.bungee.context.CommandContext;
import me.googas.commands.messages.IMessagesProvider;
import lombok.NonNull;

/** The messages provider for bungee */
public interface MessagesProvider extends IMessagesProvider<CommandContext> {

  /**
   * The message sent when the user that executed the command is not allowed to use it
   *
   * @param context the context of the command
   * @return the message to send
   */
  @NonNull
  String notAllowed(CommandContext context);

  /**
   * The message to send when the string does not match a proxied player
   *
   * @param string the string
   * @param context the context of the command
   * @return the message to send
   */
  @NonNull
  String invalidPlayer(@NonNull String string, @NonNull CommandContext context);

  /**
   * The message to send when a command that is required that the sender is a player happens to be
   * something else
   *
   * @param context the context of the command
   * @return the message
   */
  @NonNull
  String onlyPlayers(CommandContext context);
}

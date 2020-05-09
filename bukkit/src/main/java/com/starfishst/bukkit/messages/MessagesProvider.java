package com.starfishst.bukkit.messages;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.core.messages.IMessagesProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/** Provides the messages for bukkit responses */
public interface MessagesProvider extends IMessagesProvider<CommandSender, CommandContext> {

  /**
   * The message sent when a player is not found
   *
   * @param string the input string querying for a player name
   * @param context the context of the command
   * @return the message to send
   */
  String invalidPlayer(@NotNull String string, @NotNull CommandContext context);

  /**
   * The message sent when a command is executed by another entity rather than a player
   *
   * @param context the context of the command
   * @return the message to send
   */
  String playersOnly(CommandContext context);

  /**
   * The message sent when the user that executed the command is not allowed to use it
   *
   * @param context the context of the command
   * @return the message to send
   */
  String notAllowed(CommandContext context);
}

package com.starfishst.bungee.messages;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.core.messages.IMessagesProvider;
import net.md_5.bungee.api.CommandSender;

/** The messages provider for bungee */
public interface MessagesProvider extends IMessagesProvider<CommandSender, CommandContext> {

  /**
   * The message sent when the user that executed the command is not allowed to use it
   *
   * @param context the context of the command
   * @return the message to send
   */
  String notAllowed(CommandContext context);
}

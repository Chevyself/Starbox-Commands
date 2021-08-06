package me.googas.commands.system;

import me.googas.commands.messages.StarboxMessagesProvider;
import me.googas.commands.system.context.CommandContext;

/**
 * Implementation of {@link StarboxMessagesProvider} for System messages. At the moment there's no
 * new messages to include but implementation is done for the future of the module
 */
public interface MessagesProvider extends StarboxMessagesProvider<CommandContext> {}

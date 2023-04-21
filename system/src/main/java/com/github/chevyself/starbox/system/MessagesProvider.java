package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.messages.StarboxMessagesProvider;
import com.github.chevyself.starbox.system.context.CommandContext;

/**
 * Implementation of {@link StarboxMessagesProvider} for System messages. At the moment there's no
 * new messages to include but implementation is done for the future of the module
 */
public interface MessagesProvider extends StarboxMessagesProvider<CommandContext> {}

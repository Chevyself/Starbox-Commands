package com.github.chevyself.starbox.system.context.sender;

import com.github.chevyself.starbox.system.CommandManager;
import com.github.chevyself.starbox.system.SystemCommand;
import com.github.chevyself.starbox.system.context.CommandContext;

/**
 * This represents an entity which is allowed to use the {@link SystemCommand}. With the {@link
 * CommandContext} that is required by the {@link CommandManager}
 */
public interface CommandSender {}

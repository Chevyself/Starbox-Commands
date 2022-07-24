package me.googas.commands.system;

import me.googas.commands.result.StarboxResult;
import me.googas.commands.system.context.CommandContext;

/**
 * This is the extension for {@link StarboxResult} to be used in the execution of {@link
 * me.googas.commands.system.SystemCommand}.
 *
 * <p>If the {@link SystemResult} from {@link
 * me.googas.commands.system.SystemCommand#execute(CommandContext)} is null or empty no message will
 * be shown to the sender.
 */
public interface SystemResult extends StarboxResult {}

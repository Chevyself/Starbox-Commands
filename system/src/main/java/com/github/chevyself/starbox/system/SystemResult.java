package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.result.StarboxResult;
import com.github.chevyself.starbox.system.context.CommandContext;

/**
 * This is the extension for {@link StarboxResult} to be used in the execution of {@link
 * SystemCommand}.
 *
 * <p>If the {@link SystemResult} from {@link SystemCommand#execute(CommandContext)} is null or
 * empty no message will be shown to the sender.
 */
public interface SystemResult extends StarboxResult {}

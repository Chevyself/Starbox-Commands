package chevyself.github.commands.system;

import chevyself.github.commands.result.StarboxResult;
import chevyself.github.commands.system.context.CommandContext;

/**
 * This is the extension for {@link StarboxResult} to be used in the execution of {@link
 * SystemCommand}.
 *
 * <p>If the {@link SystemResult} from {@link SystemCommand#execute(CommandContext)} is null or
 * empty no message will be shown to the sender.
 */
public interface SystemResult extends StarboxResult {}

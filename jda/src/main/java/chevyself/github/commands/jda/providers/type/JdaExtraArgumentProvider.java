package chevyself.github.commands.jda.providers.type;

import chevyself.github.commands.jda.context.CommandContext;
import chevyself.github.commands.providers.type.StarboxExtraArgumentProvider;

/**
 * An extension for an extra argument provider using JDA.
 *
 * @param <T> the type of object to provide
 */
public interface JdaExtraArgumentProvider<T>
    extends StarboxExtraArgumentProvider<T, CommandContext> {}

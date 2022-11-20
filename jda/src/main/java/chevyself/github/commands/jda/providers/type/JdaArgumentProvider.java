package chevyself.github.commands.jda.providers.type;

import chevyself.github.commands.jda.context.CommandContext;
import chevyself.github.commands.providers.type.StarboxArgumentProvider;

/**
 * An extension for an argument provider using JDA.
 *
 * @param <T> the type of object to provide
 */
public interface JdaArgumentProvider<T> extends StarboxArgumentProvider<T, CommandContext> {}

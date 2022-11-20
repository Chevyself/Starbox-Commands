package chevyself.github.commands.jda.providers.type;

import chevyself.github.commands.jda.context.CommandContext;
import chevyself.github.commands.providers.type.StarboxMultipleArgumentProvider;

/**
 * An extension for a multiple argument provider using JDA.
 *
 * @param <T> the type of object to provide
 */
public interface JdaMultiArgumentProvider<T>
    extends StarboxMultipleArgumentProvider<T, CommandContext> {}

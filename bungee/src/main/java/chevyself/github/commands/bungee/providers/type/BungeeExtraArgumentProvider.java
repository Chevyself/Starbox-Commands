package chevyself.github.commands.bungee.providers.type;

import chevyself.github.commands.bungee.context.CommandContext;
import chevyself.github.commands.providers.type.StarboxExtraArgumentProvider;

/**
 * An implementation for extra argument providers in bungee.
 *
 * @param <T> the type of object to provide
 */
public interface BungeeExtraArgumentProvider<T>
    extends StarboxExtraArgumentProvider<T, CommandContext> {}

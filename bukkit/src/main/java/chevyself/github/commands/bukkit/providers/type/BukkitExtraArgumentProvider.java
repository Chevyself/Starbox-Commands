package chevyself.github.commands.bukkit.providers.type;

import chevyself.github.commands.bukkit.context.CommandContext;
import chevyself.github.commands.providers.type.StarboxExtraArgumentProvider;

/**
 * An extension for bukkit extra arguments.
 *
 * @param <O> the type of the object to provide
 */
public interface BukkitExtraArgumentProvider<O>
    extends StarboxExtraArgumentProvider<O, CommandContext> {}

package me.googas.commands.bukkit.providers.type;

import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.providers.type.EasyExtraArgumentProvider;

/**
 * An implementation for bukkit extra arguments
 *
 * @param <O> the type of the object to provide
 */
public interface BukkitExtraArgumentProvider<O>
    extends EasyExtraArgumentProvider<O, CommandContext> {}

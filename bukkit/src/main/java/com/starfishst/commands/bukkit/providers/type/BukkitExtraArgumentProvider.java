package com.starfishst.commands.bukkit.providers.type;

import com.starfishst.commands.bukkit.context.CommandContext;
import me.googas.commands.providers.type.IExtraArgumentProvider;

/**
 * An implementation for bukkit extra arguments
 *
 * @param <O> the type of the object to provide
 */
public interface BukkitExtraArgumentProvider<O> extends IExtraArgumentProvider<O, CommandContext> {}

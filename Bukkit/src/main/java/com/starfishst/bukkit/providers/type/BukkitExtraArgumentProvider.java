package com.starfishst.bukkit.providers.type;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.core.providers.type.IExtraArgumentProvider;

/**
 * An implementation for bukkit extra arguments
 *
 * @param <O> the type of the object to provide
 */
public interface BukkitExtraArgumentProvider<O> extends IExtraArgumentProvider<O, CommandContext> {}

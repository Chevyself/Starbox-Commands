package com.starfishst.commands.bungee.providers.type;

import com.starfishst.commands.bungee.context.CommandContext;
import com.starfishst.core.providers.type.IExtraArgumentProvider;

/**
 * An implementation for extra argument providers in bungee
 *
 * @param <T> the type of object to provide
 */
public interface BungeeExtraArgumentProvider<T> extends IExtraArgumentProvider<T, CommandContext> {}
package com.starfishst.bungee.providers.type;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.core.providers.type.IExtraArgumentProvider;

/**
 * An implementation for extra argument providers in bungee
 *
 * @param <T> the type of object to provide
 */
public interface BungeeExtraArgumentProvider<T> extends IExtraArgumentProvider<T, CommandContext> {}

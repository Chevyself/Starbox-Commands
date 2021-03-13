package me.googas.commands.bungee.providers.type;

import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.providers.type.IExtraArgumentProvider;

/**
 * An implementation for extra argument providers in bungee
 *
 * @param <T> the type of object to provide
 */
public interface BungeeExtraArgumentProvider<T> extends IExtraArgumentProvider<T, CommandContext> {}

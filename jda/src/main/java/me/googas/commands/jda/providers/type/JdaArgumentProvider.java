package me.googas.commands.jda.providers.type;

import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.providers.type.StarboxArgumentProvider;

/**
 * An extension for an argument provider using JDA.
 *
 * @param <T> the type of object to provide
 */
public interface JdaArgumentProvider<T> extends StarboxArgumentProvider<T, CommandContext> {}

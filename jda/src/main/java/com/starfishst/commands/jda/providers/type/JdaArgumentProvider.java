package com.starfishst.commands.jda.providers.type;

import com.starfishst.commands.jda.context.CommandContext;
import me.googas.commands.providers.type.IArgumentProvider;

/**
 * An extension for an argument provider using JDA
 *
 * @param <T> the type of object to provide
 */
public interface JdaArgumentProvider<T> extends IArgumentProvider<T, CommandContext> {}

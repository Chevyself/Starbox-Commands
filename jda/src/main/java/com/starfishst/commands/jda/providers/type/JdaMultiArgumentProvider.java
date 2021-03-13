package com.starfishst.commands.jda.providers.type;

import com.starfishst.commands.jda.context.CommandContext;
import me.googas.commands.providers.type.IMultipleArgumentProvider;

/**
 * An extension for a multiple argument provider using JDA
 *
 * @param <T> the type of object to provide
 */
public interface JdaMultiArgumentProvider<T> extends IMultipleArgumentProvider<T, CommandContext> {}

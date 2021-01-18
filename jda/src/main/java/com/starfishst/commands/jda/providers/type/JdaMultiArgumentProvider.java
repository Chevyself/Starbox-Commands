package com.starfishst.commands.jda.providers.type;

import com.starfishst.commands.jda.context.CommandContext;
import com.starfishst.core.providers.type.IMultipleArgumentProvider;

/**
 * An extension for a multiple argument provider using JDA
 *
 * @param <T> the type of object to provide
 */
public interface JdaMultiArgumentProvider<T> extends IMultipleArgumentProvider<T, CommandContext> {}

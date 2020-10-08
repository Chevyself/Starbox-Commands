package com.starfishst.jda.providers.type;

import com.starfishst.core.providers.type.IMultipleArgumentProvider;
import com.starfishst.jda.context.CommandContext;

/**
 * An extension for a multiple argument provider using JDA
 *
 * @param <T> the type of object to provide
 */
public interface JdaMultiArgumentProvider<T> extends IMultipleArgumentProvider<T, CommandContext> {}

package com.github.chevyself.starbox.jda.providers.type;

import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.providers.type.StarboxMultipleArgumentProvider;

/**
 * An extension for a multiple argument provider using JDA.
 *
 * @param <T> the type of object to provide
 */
public interface JdaMultiArgumentProvider<T>
    extends StarboxMultipleArgumentProvider<T, CommandContext> {}

package com.starfishst.jda.providers.type;

import com.starfishst.core.providers.type.IExtraArgumentProvider;
import com.starfishst.jda.context.CommandContext;

/**
 * An extension for an extra argument provider using JDA
 *
 * @param <T> the type of object to provide
 */
public interface JdaExtraArgumentProvider<T> extends IExtraArgumentProvider<T, CommandContext> {}

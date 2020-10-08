package com.starfishst.jda.providers.type;

import com.starfishst.core.providers.type.IArgumentProvider;
import com.starfishst.jda.context.CommandContext;

/**
 * An extension for an argument provider using JDA
 *
 * @param <T> the type of object to provide
 */
public interface JdaArgumentProvider<T> extends IArgumentProvider<T, CommandContext> {}

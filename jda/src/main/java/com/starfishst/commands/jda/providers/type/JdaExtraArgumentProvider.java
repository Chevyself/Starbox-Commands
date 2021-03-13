package com.starfishst.commands.jda.providers.type;

import com.starfishst.commands.jda.context.CommandContext;
import me.googas.commands.providers.type.IExtraArgumentProvider;

/**
 * An extension for an extra argument provider using JDA
 *
 * @param <T> the type of object to provide
 */
public interface JdaExtraArgumentProvider<T> extends IExtraArgumentProvider<T, CommandContext> {}

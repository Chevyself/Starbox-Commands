package me.googas.commands.providers.type;

import me.googas.commands.context.EasyCommandContext;

/**
 * This provider requires of a context to provide an object. This means that in order to provide the
 * argument for a command requires of the context in the command execution.
 *
 * <p>The direct implementations are:
 *
 * <ul>
 *   <li>{@link EasyArgumentProvider}
 *   <li>{@link EasyExtraArgumentProvider}
 *   <li>{@link EasyMultipleArgumentProvider}
 * </ul>
 *
 * @param <O> the type of object to provide
 * @param <T> the type of context that this requires to provide the object
 */
public interface EasyContextualProvider<O, T extends EasyCommandContext>
    extends EasySimpleArgumentProvider<O> {}

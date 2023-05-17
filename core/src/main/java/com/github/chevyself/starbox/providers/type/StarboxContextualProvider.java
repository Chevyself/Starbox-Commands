package com.github.chevyself.starbox.providers.type;

import com.github.chevyself.starbox.context.StarboxCommandContext;

/**
 * This provider requires of a context to provide an object. This means that in order to provide the
 * argument for a command requires of the context in the command execution.
 *
 * <p>The direct implementations are:
 *
 * <ul>
 *   <li>{@link StarboxArgumentProvider}
 *   <li>{@link StarboxExtraArgumentProvider}
 * </ul>
 *
 * @param <O> the type of object to provide
 * @param <T> the type of context that this requires to provide the object
 */
public interface StarboxContextualProvider<O, T extends StarboxCommandContext>
    extends StarboxSimpleArgumentProvider<O> {}

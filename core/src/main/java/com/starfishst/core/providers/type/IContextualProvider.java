package com.starfishst.core.providers.type;

import com.starfishst.core.context.ICommandContext;

/** A command context that is used in certain command context */
public interface IContextualProvider<O, T extends ICommandContext<?>>
    extends ISimpleArgumentProvider<O> {}

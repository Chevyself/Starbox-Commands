package me.googas.commands.providers.type;

import me.googas.commands.context.ICommandContext;

/** A command context that is used in certain command context */
public interface IContextualProvider<O, T extends ICommandContext>
    extends ISimpleArgumentProvider<O> {}

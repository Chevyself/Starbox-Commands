package me.googas.commands.providers.type;

import me.googas.commands.context.EasyCommandContext;

/** A command context that is used in certain command context */
public interface EasyContextualProvider<O, T extends EasyCommandContext>
    extends EasySimpleArgumentProvider<O> {}

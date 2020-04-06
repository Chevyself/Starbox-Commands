package com.starfishst.core;

import com.starfishst.core.context.ICommandContext;

public interface ICommandArray<C extends ICommandContext<?>> extends ISimpleCommand<C> {

    /**
     * Get the aliases of the command. Commands can have different aliases and a name
     *
     * @return the array of aliases
     */
    String[] getAliases();
}

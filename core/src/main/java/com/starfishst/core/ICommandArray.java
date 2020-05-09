package com.starfishst.core;

import com.starfishst.core.context.ICommandContext;

/**
 * A command that the aliases are in an array instead of a list
 *
 * @param <C> the context of the command
 */
public interface ICommandArray<C extends ICommandContext<?>> extends ISimpleCommand<C> {

  /**
   * Get the aliases of the command. Commands can have different aliases and a name
   *
   * @return the array of aliases
   */
  String[] getAliases();
}

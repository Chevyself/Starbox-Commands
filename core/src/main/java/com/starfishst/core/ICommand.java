package com.starfishst.core;

import com.starfishst.core.context.ICommandContext;
import java.util.List;

/** Represents a command */
public interface ICommand<C extends ICommandContext> extends ISimpleCommand<C> {

  /**
   * Get the aliases of the command. Commands can have different aliases and a name
   *
   * @return the list of aliases
   */
  List<String> getAliases();
}

package me.googas.commands;

import lombok.NonNull;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.result.IResult;

/**
 * This class represents a Command which may be executed by an user depending on the implementations
 * it may change.
 *
 * <p>// TODO add command types
 *
 * @param <C> the context that is required to run the command
 */
public interface EasyCommand<C extends EasyCommandContext> {

  /**
   * Execute the command
   *
   * @param context the context that is required to execute the command
   * @return the result of the command execution
   */
  IResult execute(@NonNull C context);

  /**
   * Check if the command can the command be recognized by the given alias. This is used because
   * commands have names and aliases, instead of asking for the name and aliases of the command just
   * check if the command has the alias
   *
   * <p>// TODO an example
   *
   * @param alias the alias to check
   * @return true if this command has the given alias
   */
  boolean hasAlias(@NonNull String alias);
}

package me.googas.commands;

import lombok.NonNull;
import me.googas.commands.context.ICommandContext;
import me.googas.commands.result.IResult;

/**
 * This class represents a Command which may be executed by an user depending on the implementations
 * it may change.
 *
 * <p>// TODO add command types
 *
 * @param <C> the context that is required to run the command
 */
public interface ICommand<C extends ICommandContext> {

  /**
   * Execute the command
   *
   * @param context the context that is required to execute the command
   * @return the result of the command execution
   */
  @NonNull
  IResult execute(@NonNull C context);
}

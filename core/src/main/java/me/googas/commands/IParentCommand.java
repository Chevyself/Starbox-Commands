package me.googas.commands;

import me.googas.commands.context.ICommandContext;
import java.util.List;
import lombok.NonNull;

/**
 * This object is just a {@link ISimpleCommand} but it contains commands inside of it, so that when
 * it gets executed in the first argument you can pass the name of the child command and it will
 * call {@link ISimpleCommand#execute(ICommandContext)} of it instead of this one.
 *
 * <p>For example: Suppose that you have a parent command with two children "game" and "core". When
 * the user inputs:
 *
 * <p>/(parent) game
 *
 * <p>It will invoke the method of the command game and not the parent
 *
 * @param <C> the type of simple commands that it contains
 */
public interface IParentCommand<C extends ISimpleCommand<?>> {

  /**
   * Get a {@link C} inside the parent matching the name in the parameter
   *
   * @param name the name that has to match
   * @return the command or null if not found
   */
  C getCommand(@NonNull String name);

  /**
   * Add a command to the parent. Instead of containing in {@link ICommandManager} it will be in
   * this parent
   *
   * @param command the command to add in the list
   */
  default void addCommand(@NonNull C command) {
    getCommands().add(command);
  }

  /**
   * Get the {@link List} of {@link C} inside of the parent. This means getting all the commands
   * that were registered in here instead of the {@link ICommandManager}
   *
   * @return the {@link List} of {@link C}
   */
  @NonNull
  List<C> getCommands();
}

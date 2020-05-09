package com.starfishst.core;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a {@link ICommand} with a bunch of them inside of it also called
 * Sub-commands
 */
public interface IParentCommand<C extends ISimpleCommand<?>> {

  /**
   * Get a {@link ICommand} inside the parent
   *
   * @param name the name of the command
   * @return the command or null if not found
   */
  @Nullable
  C getCommand(@NotNull String name);

  /**
   * Add a command to the parent
   *
   * @param command the command to add
   */
  default void addCommand(@NotNull C command) {
    getCommands().add(command);
  }

  /**
   * Get the {@link List} of {@link ICommand} inside of the parent
   *
   * @return the {@link List} of {@link ICommand}
   */
  @NotNull
  List<C> getCommands();
}

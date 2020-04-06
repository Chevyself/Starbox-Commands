package com.starfishst.core;

import com.starfishst.core.context.ICommandContext;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a {@link ICommand} with a bunch of them inside of it also called
 * Subcommands
 */
public interface IParentCommand<C extends ISimpleCommand<?>, K extends ICommandContext<?>> {

    /**
     * Get the {@link List} of {@link ICommand} inside of the parent
     *
     * @return the {@link List} of {@link ICommand}
     */
    @NotNull
    List<C> getCommands();

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
}

package com.starfishst.core.context;

import com.starfishst.core.utils.Lots;
import org.jetbrains.annotations.NotNull;

/**
 * The context of a command
 *
 * @param <O> the sender of the command
 */
public interface ICommandContext<O> {

    /**
     * Get the sender of the command
     *
     * @return the sender of the command
     */
    @NotNull
    O getSender();

    /**
     * Get the joined strings of the command
     *
     * @return the joined strings
     */
    @NotNull
    String getString();

    /**
     * Get the joined strings of the command
     *
     * @return the joined strings
     */
    @NotNull
    String[] getStrings();

    /**
     * Get's if the command was executed using a flag
     *
     * @param flag the flag to check
     * @return true if the command was executed with a flag
     */
    boolean hasFlag(@NotNull String flag);

    /**
     * Get the joined strings from a certain position
     *
     * @param position the position to get the string from
     * @return an array of strings empty if none
     */
    @NotNull
    default String[] getStringsFrom(int position) {
        return Lots.arrayFrom(position, this.getStrings());
    }
}

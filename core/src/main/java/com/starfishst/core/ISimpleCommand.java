package com.starfishst.core;

import com.starfishst.core.arguments.Argument;
import com.starfishst.core.arguments.type.ISimpleArgument;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.result.IResult;

import java.lang.reflect.Method;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ISimpleCommand<C extends ICommandContext<?>> {

    /**
     * Get the string to use as argument in a position
     *
     * @param argument the argument requested
     * @param context  the context of the command
     */
    @Nullable
    static String getArgument(@NotNull Argument argument, @NotNull ICommandContext<?> context) {
        String[] strings = context.getStrings();
        if (strings.length - 1 < argument.getPosition()) {
            if (argument.isRequired() & argument.getSuggestions(context).size() > 0) {
                return argument.getSuggestions(context).get(0);
            } else {
                return null;
            }
        } else {
            return strings[argument.getPosition()];
        }
    }

    @NotNull
    IResult execute(@NotNull C context);

    /**
     * Get the class instance of a command
     *
     * @return the class instance of a command
     */
    Object getClazz();

    /**
     * Get the method to run a command
     *
     * @return the method of a command
     */
    Method getMethod();

    /**
     * Get the {@link List} of {@link Argument} from the command
     *
     * @return the {@link List} of {@link Argument}
     */
    List<ISimpleArgument> getArguments();
}

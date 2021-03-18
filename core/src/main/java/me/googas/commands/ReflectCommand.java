package me.googas.commands;

import lombok.NonNull;
import me.googas.commands.arguments.Argument;
import me.googas.commands.arguments.ExtraArgument;
import me.googas.commands.arguments.ISimpleArgument;
import me.googas.commands.arguments.MultipleArgument;
import me.googas.commands.context.ICommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.exceptions.MissingArgumentException;
import me.googas.commands.messages.IMessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.providers.type.IArgumentProvider;
import me.googas.commands.result.IResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * A reflect command is a command that is parsed using Java reflection. That's why this includes methods as {@link #getMethod} or {@link #getObject}
 */
public interface ReflectCommand<C extends ICommandContext> extends NewICommand<C> {

    /**
     * Get the string that will be used to get the object to pass to the command method as a parameter
     * (Check {@link IArgumentProvider}).
     *
     * @param argument the argument requested in the position
     * @param context the context of the command
     * @return It will try to get the string in the argument position. If the string in the context is
     *     null and the argument is not required and it does not have any suggestions it will return
     *     null else it will return the first suggestion. If the string in the context is not null
     *     then it will return that one
     */
    static String getArgument(@NonNull Argument<?> argument, @NonNull ICommandContext context) {
        String[] strings = context.getStrings();
        if (strings.length - 1 < argument.getPosition()) {
            if (!argument.isRequired() & argument.getSuggestions(context).size() > 0) {
                return argument.getSuggestions(context).get(0);
            } else {
                return null;
            }
        } else {
            return strings[argument.getPosition()];
        }
    }

    /**
     * Get the objects that should be used in the parameters to invoke {@link #getMethod()}. For each
     * {@link ICommandContext#getStrings()} it will try to get one object, unless the argument in the
     * position of the string is a {@link MultipleArgument}. Check the {@link #getRegistry()} to get
     * which classes can be provided as an object.
     *
     * @param context the context to get the parameters (strings)
     * @return the objects to use as parameters in the {@link #getMethod()}
     * @throws ArgumentProviderException if the argument could not be provided more in the exception
     *     class
     * @throws MissingArgumentException if the command is missing an argument. Also it will try to
     *     return the result as a help message to get a correct input from the user
     */
    @NonNull
    default Object[] getObjects(C context)
            throws MissingArgumentException, ArgumentProviderException {
        Object[] objects = new Object[getArguments().size()];
        for (int i = 0; i < getArguments().size(); i++) {
            ISimpleArgument<?> argument = getArguments().get(i);
            if (argument instanceof ExtraArgument<?>) {
                objects[i] = getRegistry().getObject(argument.getClazz(), context);
            } else if (argument instanceof MultipleArgument<?>) {
                String[] strings = context.getStringsFrom(((MultipleArgument<?>) argument).getPosition());
                if (strings.length < ((MultipleArgument<?>) argument).getMinSize()) {
                    throw new MissingArgumentException(
                            this.getMessagesProvider()
                                    .missingStrings(
                                            ((MultipleArgument<?>) argument).getName(),
                                            ((MultipleArgument<?>) argument).getDescription(),
                                            ((MultipleArgument<?>) argument).getPosition(),
                                            ((MultipleArgument<?>) argument).getMinSize(),
                                            ((MultipleArgument<?>) argument).getMinSize() - strings.length,
                                            context));
                }
                if (((MultipleArgument<?>) argument).getMaxSize() != -1
                        && ((MultipleArgument<?>) argument).getMaxSize() < strings.length) {
                    i = ((MultipleArgument<?>) argument).getMaxSize();
                }
                objects[i] = getRegistry().fromStrings(strings, argument.getClazz(), context);
            } else if (argument instanceof Argument<?>) {
                String string = getArgument((Argument<?>) argument, context);
                if (string == null && ((Argument<?>) argument).isRequired()) {
                    throw new MissingArgumentException(
                            getMessagesProvider()
                                    .missingArgument(
                                            ((Argument<?>) argument).getName(),
                                            ((Argument<?>) argument).getDescription(),
                                            ((Argument<?>) argument).getPosition(),
                                            context));
                } else if (string == null && !((Argument<?>) argument).isRequired()) {
                    objects[i] = null;
                } else if (string == null) {
                    objects[i] = null;
                } else {
                    objects[i] = getRegistry().fromString(string, argument.getClazz(), context);
                }
            }
        }
        return objects;
    }

    /**
     * Get the argument of certain position. A basic loop checking if the {@link
     * Argument#getPosition()} matches the queried position. Ignore the extra arguments as those don't
     * have positions
     *
     * @param position the position to get the argument from
     * @return the argument if exists else null
     */
    default Argument<?> getArgument(int position) {
        for (ISimpleArgument<?> argument : this.getArguments()) {
            if (argument instanceof Argument && ((Argument<?>) argument).getPosition() == position) {
                return (Argument<?>) argument;
            }
        }
        return null;
    }

    /**
     * Get the method to run a command. This is annotated with the respective @Command annotation and
     * it is required to call the command
     *
     * // TODO add example
     *
     * @return the method to execute a command
     */
    @NonNull
    Method getMethod();

    /**
     * Get the instance of a class that contains a command. It is required to call the {@link Method#invoke(Object,
     * Object...)} because non static methods cannot be called without it, static methods have no
     * problem
     *
     * // TODO add example
     *
     * @return the class instance of a command method
     */
    @NonNull
    Object getObject();

    /**
     * Get the {@link List} of the arguments for the command. It is used in {@link #getArgument(int)}
     * therefore in {@link #getObjects(ICommandContext)}
     *
     * @return the {@link List} of {@link ISimpleArgument}
     */
    @NonNull
    List<ISimpleArgument<?>> getArguments();

    /**
     * Get the registry of providers. Needed to get the objects to pass in the method invoke.
     *
     * @return the registry of providers for the command
     */
    @NonNull
    ProvidersRegistry<C> getRegistry();

    /**
     * Get the messages provider for the command. Needed to send helpful messages to help the user
     * execute the command correctly
     *
     * @return the messages provider
     */
    @NonNull
    IMessagesProvider<C> getMessagesProvider();

    /**
     * Executes the command and gives a result of its execution
     *
     * @param context the context of the command
     * @return the result of the command execution
     */
    @Override
    default @NonNull IResult execute(@NonNull C context) {
        try {
            return (IResult) getMethod().invoke(getObject(), getObjects(context));
        } catch (MissingArgumentException
                | ArgumentProviderException
                | IllegalAccessException
                | InvocationTargetException e) {
            return () -> "Result in error: " + e.getMessage();
        }
    }
}
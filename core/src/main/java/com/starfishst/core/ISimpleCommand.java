package com.starfishst.core;

import com.starfishst.core.arguments.Argument;
import com.starfishst.core.arguments.ExtraArgument;
import com.starfishst.core.arguments.ISimpleArgument;
import com.starfishst.core.arguments.MultipleArgument;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.exceptions.MissingArgumentException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.core.result.IResult;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This object represents a command this is the way to invoke the method.
 *
 * @param <C> the type of command context that the command has to use to be executed
 */
public interface ISimpleCommand<C extends ICommandContext> {

  /**
   * Get the string that will be used to get the object to pass to the command method as a parameter
   * (Check {@link com.starfishst.core.providers.type.IArgumentProvider}).
   *
   * @param argument the argument requested in the position
   * @param context the context of the command
   * @return It will try to get the string in the argument position. If the string in the context is
   *     null and the argument is not required and it does not have any suggestions it will return
   *     null else it will return the first suggestion. If the string in the context is not null
   *     then it will return that one
   */
  @Nullable
  static String getArgument(@NotNull Argument<?> argument, @NotNull ICommandContext context) {
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
   * Executes the command and gives a result of its execution
   *
   * @param context the context of the command
   * @return the result of the command execution
   */
  @NotNull
  default IResult execute(@NotNull C context) {
    try {
      return (IResult) getMethod().invoke(getClazz(), getObjects(context));
    } catch (MissingArgumentException
        | ArgumentProviderException
        | IllegalAccessException
        | InvocationTargetException e) {
      return () -> "Result in error: " + e.getMessage();
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
  @NotNull
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
  @Nullable
  default Argument<?> getArgument(int position) {
    for (ISimpleArgument<?> argument : this.getArguments()) {
      if (argument instanceof Argument && ((Argument<?>) argument).getPosition() == position) {
        return (Argument<?>) argument;
      }
    }
    return null;
  }

  /**
   * Get the class instance of a command. It is required to call the {@link Method#invoke(Object,
   * Object...)} because non static methods cannot be called without it, static methods have no
   * problem
   *
   * @return the class instance of a command method
   */
  @NotNull
  Object getClazz();

  /**
   * Get the method to run a command. This is annotated with the respective @Command annotation and
   * it is required for obvious reasons to call the command
   *
   * @return the method of a command
   */
  @NotNull
  Method getMethod();

  /**
   * Get the {@link List} of the arguments for the command. It is used in {@link #getArgument(int)}
   * therefore in {@link #getObjects(ICommandContext)}
   *
   * @return the {@link List} of {@link ISimpleArgument}
   */
  @NotNull
  List<ISimpleArgument<?>> getArguments();

  /**
   * Get the registry of providers. Needed to get the objects to pass in the method invoke.
   *
   * @return the registry of providers for the command
   */
  @NotNull
  ProvidersRegistry<C> getRegistry();

  /**
   * Get the messages provider for the command. Needed to send helpful messages to help the user
   * execute the command correctly
   *
   * @return the messages provider
   */
  @NotNull
  IMessagesProvider<C> getMessagesProvider();
}

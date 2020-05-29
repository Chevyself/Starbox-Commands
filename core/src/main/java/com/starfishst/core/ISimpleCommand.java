package com.starfishst.core;

import com.starfishst.core.arguments.Argument;
import com.starfishst.core.arguments.ExtraArgument;
import com.starfishst.core.arguments.MultipleArgument;
import com.starfishst.core.arguments.type.ISimpleArgument;
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
 * The simples for of commands
 *
 * @param <C> the command context
 */
public interface ISimpleCommand<C extends ICommandContext> {

  /**
   * Get the string to use as argument in a position
   *
   * @param argument the argument requested
   * @param context the context of the command
   * @return the string that matches the requirements of the argument
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
   * Executes the command and gives a result
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
   * Get the objects that should be used in the parameters to invoke {@link #getMethod()}
   *
   * @param context the context to get the parameters
   * @return the parameters
   * @throws ArgumentProviderException if the argument could not be provided
   * @throws MissingArgumentException if the command is missing an argument
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
        objects[i] =
            getRegistry()
                .fromStrings(
                    context.getStringsFrom(((MultipleArgument<?>) argument).getPosition()),
                    argument.getClazz(),
                    context);
      } else if (argument instanceof Argument<?>) {
        String string = getArgument((Argument<?>) argument, context);
        if (string == null && ((Argument<?>) argument).isRequired()) {
          throw new MissingArgumentException(
              getMessagesProvider()
                  .missingArgument(
                      ((Argument<?>) argument).getName(),
                      ((Argument<?>) argument).getDescription(),
                      ((Argument<?>) argument).getPosition()));
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
   * Get the class instance of a command
   *
   * @return the class instance of a command
   */
  @NotNull
  Object getClazz();

  /**
   * Get the method to run a command
   *
   * @return the method of a command
   */
  @NotNull
  Method getMethod();

  /**
   * Get the {@link List} of {@link Argument} from the command
   *
   * @return the {@link List} of {@link Argument}
   */
  @NotNull
  List<ISimpleArgument<?>> getArguments();

  /**
   * Get the registry of providers
   *
   * @return the registry of providers
   */
  @NotNull
  ProvidersRegistry<C> getRegistry();

  /**
   * Get the messages provider for the command
   *
   * @return the messages provider
   */
  @NotNull
  IMessagesProvider<C> getMessagesProvider();
}

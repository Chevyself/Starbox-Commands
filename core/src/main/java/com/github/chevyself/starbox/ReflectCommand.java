package com.github.chevyself.starbox;

import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.arguments.ArgumentBehaviour;
import com.github.chevyself.starbox.arguments.SingleArgument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.exceptions.MissingArgumentException;
import com.github.chevyself.starbox.messages.StarboxMessagesProvider;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import com.github.chevyself.starbox.providers.type.StarboxArgumentProvider;
import com.github.chevyself.starbox.result.StarboxResult;
import com.github.chevyself.starbox.util.Pair;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;

/**
 * A reflection command is a command that is parsed using Java reflection. That's why this includes
 * methods as {@link #getMethod} or {@link #getObject}
 *
 * @param <C> the type of context that is required to run the command
 * @param <T> the type of command that can be children
 */
public interface ReflectCommand<C extends StarboxCommandContext, T extends StarboxCommand<C, T>>
    extends StarboxCommand<C, T> {

  /**
   * Get the string that will be used to get the object to pass to the command method as a parameter
   * (Check {@link StarboxArgumentProvider}).
   *
   * @param argument the argument that requires the object
   * @param context the context of the command execution
   * @param lastIndex where do arguments originate
   * @return the obtained string and the amount to increase the last index
   */
  @NonNull
  static Pair<String, Integer> getArgument(
      @NonNull SingleArgument<?> argument, @NonNull StarboxCommandContext context, int lastIndex) {
    List<String> arguments = context.getCommandLineParser().getArguments();
    String string = null;
    int increase = 0;
    if (arguments.size() - 1 < argument.getPosition() + lastIndex) {
      if (!argument.isRequired() & argument.getSuggestions(context).size() > 0) {
        string = argument.getSuggestions(context).get(0);
      }
    } else {
      if (argument.getBehaviour().equals(ArgumentBehaviour.CONTINUOUS)) {
        string = String.join(" ", arguments.subList(argument.getPosition(), arguments.size()));
      } else {
        string = arguments.get(argument.getPosition() + lastIndex);
      }
    }
    return new Pair<>(string, increase);
  }

  /**
   * Get the objects that should be used in the parameters to invoke {@link #getMethod()}. For each
   * {@link StarboxCommandContext#getStrings()} it will try to get one object.
   *
   * @param context the context to get the parameters {@link StarboxCommandContext#getStrings()}
   * @return the objects to use as parameters in the {@link #getMethod()}
   * @throws ArgumentProviderException if the argument could not be provided, see {@link
   *     ArgumentProviderException}
   * @throws MissingArgumentException if the command is missing an argument. Also, it will try to
   *     return the result as a help message to get a correct input from the user, see {@link
   *     MissingArgumentException}
   */
  @NonNull
  default Object[] getObjects(C context)
      throws MissingArgumentException, ArgumentProviderException {
    Object[] objects = new Object[this.getArguments().size()];
    int lastIndex = 0;
    for (int i = 0; i < this.getArguments().size(); i++) {
      Pair<Object, Integer> pair =
          this.getArguments()
              .get(i)
              .process(this.getProvidersRegistry(), this.getMessagesProvider(), context, lastIndex);
      objects[i] = pair.getA();
      lastIndex += pair.getB();
    }
    return objects;
  }

  /**
   * Get the argument of certain position. A basic loop checking if the {@link SingleArgument}
   * position matches the queried position. Ignore the extra arguments as those don't have positions
   *
   * @param position the position to get the argument of
   * @return the argument if exists, empty otherwise
   */
  @NonNull
  default Optional<SingleArgument<?>> getArgument(int position) {
    SingleArgument<?> singleArgument = null;
    for (Argument<?> argument : this.getArguments()) {
      if (argument instanceof SingleArgument
          && ((SingleArgument<?>) argument).getPosition() == position) {
        singleArgument = (SingleArgument<?>) argument;
      }
    }
    return Optional.ofNullable(singleArgument);
  }

  /**
   * Get the method to run a command. This is annotated with the respective @Command annotation, and
   * it is required to call the command
   *
   * @return the method to execute a command
   */
  @NonNull
  Method getMethod();

  /**
   * Get the instance of a class that contains a command. It is required to call the {@link
   * Method#invoke(Object, Object...)} because non-static methods cannot be called without it,
   * static methods have no problem
   *
   * @return the class instance of a command method
   */
  @NonNull
  Object getObject();

  /**
   * Get the {@link List} of the arguments for the command. It is used in {@link #getArgument(int)}
   * therefore in {@link #getObjects(StarboxCommandContext)}
   *
   * @return the {@link List} of {@link Argument}
   */
  @NonNull
  List<Argument<?>> getArguments();

  /**
   * Get the registry of providers. Needed to get the objects to pass in the method invoke.
   *
   * @return the registry of providers for the command
   */
  @NonNull
  ProvidersRegistry<C> getProvidersRegistry();

  /**
   * Get the messages' provider for the command. Needed to send helpful messages to help the user
   * execute the command correctly
   *
   * @return the messages provider
   */
  @NonNull
  StarboxMessagesProvider<C> getMessagesProvider();

  /**
   * Executes the command and gives a result of its execution.
   *
   * @param context the context of the command
   * @return the result of the command execution
   */
  @Override
  default StarboxResult execute(@NonNull C context) {
    try {
      return (StarboxResult) this.getMethod().invoke(this.getObject(), this.getObjects(context));
    } catch (MissingArgumentException
        | ArgumentProviderException
        | IllegalAccessException
        | InvocationTargetException e) {
      return () -> Optional.of("Result in error: " + e.getMessage());
    }
  }
}

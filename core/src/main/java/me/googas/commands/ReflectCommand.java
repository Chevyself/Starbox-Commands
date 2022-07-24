package me.googas.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.arguments.Argument;
import me.googas.commands.arguments.ExtraArgument;
import me.googas.commands.arguments.SingleArgument;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.exceptions.MissingArgumentException;
import me.googas.commands.messages.StarboxMessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.providers.type.StarboxArgumentProvider;
import me.googas.commands.result.StarboxResult;
import me.googas.commands.util.JoinedString;
import me.googas.commands.util.Strings;

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
   * @param argument the argument requested in the position
   * @param context the context of the command execution
   * @return a {@link Optional} instance wrapping the nullable argument. It will try to get the
   *     string in the argument position. If the string in the context is null and the argument is
   *     not required, and it does not have any suggestions it will return null else it will return
   *     the first suggestion. If the string in the context is not null then it will return that one
   */
  @NonNull
  static Optional<String> getArgument(
      @NonNull SingleArgument<?> argument, @NonNull StarboxCommandContext context) {
    String[] strings = context.getStrings();
    String string = null;
    if (strings.length - 1 < argument.getPosition()) {
      if (!argument.isRequired() & argument.getSuggestions(context).size() > 0) {
        string = argument.getSuggestions(context).get(0);
      }
    } else {
      string = strings[argument.getPosition()];
    }
    return Optional.ofNullable(string);
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
      Argument<?> argument = this.getArguments().get(i);
      if (argument instanceof ExtraArgument<?>) {
        objects[i] = this.getRegistry().getObject(argument.getClazz(), context);
      } else if (argument instanceof SingleArgument<?>) {
        SingleArgument<?> singleArgument = (SingleArgument<?>) argument;
        String[] strings = context.getStrings();
        String string = null;
        if (strings.length - 1 < singleArgument.getPosition() + lastIndex) {
          if (!singleArgument.isRequired() & singleArgument.getSuggestions(context).size() > 0) {
            string = singleArgument.getSuggestions(context).get(0);
          }
        } else {
          switch (singleArgument.getBehaviour()) {
            case CONTINUOUS:
              string =
                  Strings.join(context.getStringsFrom(singleArgument.getPosition() + lastIndex));
              break;
            case MULTIPLE:
              JoinedString joined =
                  Strings.group(context.getStringsFrom(singleArgument.getPosition() + lastIndex))
                      .get(0);
              string = joined.getString();
              lastIndex += joined.getSize();
              break;
            case NORMAL:
            default:
              string = strings[singleArgument.getPosition() + lastIndex];
          }
        }
        if (string == null && ((SingleArgument<?>) argument).isRequired()) {
          throw new MissingArgumentException(
              this.getMessagesProvider()
                  .missingArgument(
                      ((SingleArgument<?>) argument).getName(),
                      ((SingleArgument<?>) argument).getDescription(),
                      ((SingleArgument<?>) argument).getPosition(),
                      context));
        } else if (string == null && !((SingleArgument<?>) argument).isRequired()) {
          objects[i] = null;
        } else if (string == null) {
          objects[i] = null;
        } else {
          objects[i] = this.getRegistry().fromString(string, argument.getClazz(), context);
        }
      }
    }
    return objects;
  }

  /**
   * Get the argument of certain position. A basic loop checking if the {@link SingleArgument}
   * position matches the queried position. Ignore the extra arguments as those don't have positions
   *
   * @param position the position to get the argument of
   * @return the argument if exists, null otherwise
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
   * Method#invoke(Object, Object...)} because non static methods cannot be called without it,
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
  ProvidersRegistry<C> getRegistry();

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

package me.googas.commands.arguments;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.annotations.Multiple;
import me.googas.commands.annotations.Optional;
import me.googas.commands.annotations.Required;
import me.googas.commands.exceptions.CommandRegistrationException;

/**
 * An argument can change the output of a command and this type is used for commands that are parsed
 * using reflection.
 *
 * <p>There's three main types of arguments:
 *
 * <ul>
 *   <li>{@link ExtraArgument} which is not exactly given by the user but by the context of the
 *       command execution, that is why it does not require an annotation as you can see in {@link
 *       #isEmpty(Annotation[])} if this method returns true it will be considered as an {@link
 *       ExtraArgument}
 *   <li>{@link SingleArgument} this argument expects an user input unless it is annotated with
 *       {@link Optional}. It has a place inside the command usage: [prefix][command] [argument]
 *       [argument] [argument]
 *   <li>{@link MultipleArgument} just like a {@link SingleArgument} but it has many places in a
 *       command which means that multiple inputs are allowed: [prefix][command] [argument 1]
 *       [argument 1] [argument 1] [argument 2] or [prefix][command] [argument 1] [argument 2]
 *       [argument 2] [argument 2] for easier understanding
 * </ul>
 *
 * To know how arguments are parsed you can check {@link #parseArguments(Method)} or {@link
 * #parseArguments(Class[], Annotation[][])} and to know how a single argument is parsed see {@link
 * #parseArgument(Class, Annotation[], int)}
 *
 * <p>Here's an example:
 *
 * <pre>
 *  public class ArgumentsSample {
 *
 *     public static void main(String[] args) throws NoSuchMethodException {
 *         // Parsing from the AMethod of this same class
 *         List<Argument<?>> arguments = Argument.parseArguments(ArgumentsSample.class.getMethod("AMethod", EasyCommandContext.class, String.class, String.class, String[].class));
 *         for (Argument<?> argument : arguments) {
 *             System.out.println("argument = " + argument);
 *         }
 *         // Output:
 *         // argument = ExtraArgument{clazz=interface me.googas.commands.context.EasyCommandContext}
 *         // argument = SingleArgument{name='No name provided', description='No description provided', suggestions=[], clazz=class java.lang.String, required=true, position=0}
 *         // argument = SingleArgument{name='No name provided', description='No description provided', suggestions=[], clazz=class java.lang.String, required=false, position=1}
 *         // argument = MultipleArgument{minSize=1, maxSize=-1} SingleArgument{name='No name provided', description='No description provided', suggestions=[], clazz=class [Ljava.lang.String;, required=true, position=2}
 *     }
 *
 *     public void AMethod(EasyCommandContext context, @Required String name, @Optional String description, @Required @Multiple String[] messages) {
 *         // Has 4 arguments
 *         // An ExtraArgument: the context
 *         // Two SingleArgument: The name and description
 *         // A MultipleArgument: the messages
 *     }
 * }
 *  </pre>
 *
 *  A list of arguments may also be used to get the usage of a command. From <a href="https://en.wikipedia.org/wiki/Usage_message">Wikipedia</a>
 *
 *  <ul>
 *      <li>Required arguments are indicated with angles brackets: '<>' ex: '<name>'</li>
 *      <li>Optional arguments are indicated with square brackets: '[]' ex: '[name]'</li>
 *      <li>Flags start with the vertical bar and are indicated using square brackets: ex: [-f]</li>
 *      <li>Flags with a value are just like normal flags but the value is separated with a single space ' ' ex: [-f <value>] They can also contain a
 *      key and a value as such: [-f <key>=<value>]</li> with the equals sign '=' separating the key and value
 *  </ul>
 *
 * Here's an example:
 *
 * Usage: command [-f] [--help | -H] [-c <child_name>] [-p <number>] <arg1> <arg2> [arg3]
 *
 * @param <O> the type of the class that the argument has to supply
 */
public interface Argument<O> {

  /**
   * Parse the arguments from a {@link Method}. This will use {@link #parseArguments(Class[],
   * Annotation[][])} using {@link Method#getParameterTypes()} and {@link
   * Method#getParameterAnnotations()}
   *
   * @param method the method to parse the arguments from
   * @return the list of arguments from the method
   */
  static List<Argument<?>> parseArguments(@NonNull Method method) {
    return parseArguments(method.getParameterTypes(), method.getParameterAnnotations());
  }

  /**
   * Parse the list {@link Argument} from an array of parameters and its annotations.
   *
   * <p>To parse each argument this will iterate thru the parameter class and its annotations
   * checking that {@link #isEmpty(Annotation[])} if this method results true it will return an
   * {@link ExtraArgument} else it will use the method {@link #parseArgument(Class, Annotation[],
   * int)} and the position will increase. If the argument resulting from this method is {@link
   * MultipleArgument} the position will increase as much as {@link Multiple#min()} and {@link
   * Multiple#max()} requires.
   *
   * @param parameters the array of parameters to parse to {@link Argument}
   * @param annotations the array of annotations for each parameter
   * @return the list of parsed {@link Argument}
   */
  @NonNull
  static List<Argument<?>> parseArguments(
      @NonNull final Class<?>[] parameters, @NonNull final Annotation[][] annotations) {
    List<Argument<?>> arguments = new ArrayList<>();
    int position = 0;
    for (int i = 0; i < parameters.length; i++) {
      Annotation[] paramAnnotations = annotations[i];
      if (isEmpty(paramAnnotations)) {
        arguments.add(i, new ExtraArgument<>(parameters[i]));
      } else {
        SingleArgument<?> argument = parseArgument(parameters[i], annotations[i], position);
        arguments.add(i, argument);
        if (argument instanceof MultipleArgument) {
          position = +((MultipleArgument<?>) argument).getMinSize();
        } else {
          position++;
        }
      }
    }
    return arguments;
  }

  /**
   * Parse the argument using the parameter class and the annotations of the parameter.
   *
   * <p>This requires that the argument has any of the two annotations: {@link Required} or {@link
   * Optional} if that is not the case then an exception will be thrown.
   *
   * <p>The annotation {@link Multiple} is obtained using {@link #getMultiple(Annotation[])}
   *
   * <p>A simple iteration will be done in order to get any of the two annotations first then in
   * {@link #getArgument(Class, int, Multiple, boolean, String, String, List)} the boolean
   * represents whether the argument is required
   *
   * @param parameter the class of the parameter
   * @param annotations the annotations of the parameter
   * @param position the position of the parameter given by the {@link #parseArguments(Class[],
   *     Annotation[][])}
   * @return the final parsed argument it could be {@link SingleArgument} or {@link
   *     MultipleArgument}
   * @throws CommandRegistrationException if the parameter does not contain an annotation such as
   *     {@link Required} or {@link Optional}
   */
  @NonNull
  static SingleArgument<?> parseArgument(
      @NonNull Class<?> parameter, @NonNull Annotation[] annotations, int position) {
    Multiple multiple = getMultiple(annotations);
    for (Annotation annotation : annotations) {
      if (annotation instanceof Required) {
        String name = ((Required) annotation).name();
        String description = ((Required) annotation).description();
        List<String> suggestions = Arrays.asList(((Required) annotation).suggestions());
        return getArgument(parameter, position, multiple, true, name, description, suggestions);
      } else if (annotation instanceof Optional) {
        String name = ((Optional) annotation).name();
        String description = ((Optional) annotation).description();
        List<String> suggestions = Arrays.asList(((Optional) annotation).suggestions());
        return getArgument(parameter, position, multiple, false, name, description, suggestions);
      }
    }
    throw new CommandRegistrationException(
        "SingleArgument could not be parsed for "
            + parameter
            + " because it may not contain the annotations "
            + Required.class
            + " or "
            + Optional.class);
  }

  /**
   * Checks if the annotations array does not contain either the {@link Required} or {@link
   * Optional} annotations. Loops around the array and check if it is either one.
   *
   * @param annotations the array of annotations to check
   * @return true if the array does not contain either of both annotations
   */
  static boolean isEmpty(@NonNull Annotation[] annotations) {
    for (Annotation annotation : annotations) {
      if (annotation instanceof Required || annotation instanceof Optional) return false;
    }
    return true;
  }

  /**
   * Get the annotation {@link Multiple} from an array of annotations
   *
   * @param annotations the array of annotations
   * @return the annotation if the array contains it else null
   */
  static Multiple getMultiple(@NonNull Annotation[] annotations) {
    for (Annotation annotation : annotations) {
      if (annotation instanceof Multiple) {
        return (Multiple) annotation;
      }
    }
    return null;
  }

  /**
   * This gets the final instance of the argument. Called by {@link #parseArgument(Class,
   * Annotation[], int)} basically this gives either {@link MultipleArgument} or {@link
   * SingleArgument} it is check in the {@link #parseArgument(Class, Annotation[], int)} method if
   * the parameter contains the annotation {@link Multiple}
   *
   * @param parameter the class of the parameter
   * @param position the position of the argument
   * @param multiple the annotation required to get an {@link MultipleArgument} if it is null it
   *     will be a {@link SingleArgument}
   * @param required whether the argument is required (if it has the annotation {@link Required} it
   *     is required)
   * @param name the name of the argument
   * @param description the description of the argument
   * @param suggestions the suggestions for the argument
   * @return the argument instance if multiple is true it will be a {@link MultipleArgument} else
   *     just a {@link SingleArgument}
   */
  @NonNull
  static SingleArgument<?> getArgument(
      @NonNull Class<?> parameter,
      int position,
      Multiple multiple,
      boolean required,
      @NonNull String name,
      @NonNull String description,
      @NonNull List<String> suggestions) {
    if (multiple != null) {
      return new MultipleArgument<>(
          name,
          description,
          suggestions,
          parameter,
          true,
          position,
          multiple.min(),
          multiple.max());
    } else {
      return new SingleArgument<>(name, description, suggestions, parameter, required, position);
    }
  }

  /**
   * Get the class of the argument.
   *
   * <pre>
   * public void AMethod(EasyCommandContext context) {
   *       // The class of the argument is EasyCommandContext
   * }
   * </pre>
   *
   * @return the class of the argument
   */
  @NonNull
  Class<O> getClazz();
}

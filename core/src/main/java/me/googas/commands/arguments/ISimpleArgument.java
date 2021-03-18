package me.googas.commands.arguments;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.annotations.Multiple;
import me.googas.commands.annotations.Optional;
import me.googas.commands.annotations.Required;
import me.googas.commands.exceptions.CommandRegistrationException;

/** A simple argument only requires the class of the argument */
// TODO check documentation for this class
public interface ISimpleArgument<O> {

  /**
   * Get the class of the argument
   *
   * @return the class of the argument
   */
  @NonNull
  Class<O> getClazz();

  /**
   * Parse the arguments of a command. This method looks for the parameters of the method using
   * reflection. If a parameter is not annotated with anything (check {@link
   * #isEmpty(Annotation[])}) it will be considered as a {@link ExtraArgument} else it is a {@link
   * Argument}. When an argument is annotated with {@link Multiple} it means that the argument
   * requires multiple strings, the strings will be taken from the position of the argument. With
   * each argument a position will increase except if the argument is {@link ExtraArgument}
   *
   * @param parameters the parameters of the command method
   * @param annotations the annotations of the parameters of the command method
   * @return the list of parsed {@link ISimpleArgument} empty if there's none
   */
  @NonNull
  static List<ISimpleArgument<?>> parseArguments(
      @NonNull final Class<?>[] parameters, @NonNull final Annotation[][] annotations) {
    List<ISimpleArgument<?>> arguments = new ArrayList<>();
    int position = 0;
    for (int i = 0; i < parameters.length; i++) {
      Annotation[] paramAnnotations = annotations[i];
      if (isEmpty(paramAnnotations)) {
        arguments.add(i, new ExtraArgument<>(parameters[i]));
      } else {
        Argument<?> argument = parseArgument(parameters[i], annotations[i], position);
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
   * Parse the argument using the parameter class and the annotation. It is called by <br>
   * {@link #parseArguments(Class[], Annotation[][])} to get each single instance of argument. If
   * the parameter does not contain an annotation this should not be called use <br>
   * {@link ExtraArgument} instead
   *
   * @param parameter the parameter of the method of the command
   * @param annotations the annotations of the parameter used to determinate if it is a required
   *     argument or not. Check {@link Required} and {@link Optional}
   * @param position the position of the parameter given by the {@link #parseArguments(Class[],
   *     Annotation[][])}
   * @return the final parsed argument
   * @throws CommandRegistrationException if the parameter does not contain an annotation such as
   *     {@link Required} or {@link Optional}
   */
  @NonNull
  static Argument<?> parseArgument(
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
        "Argument could not be parsed for "
            + parameter
            + " because it may not contain the annotations "
            + Required.class
            + " or "
            + Optional.class);
  }

  /**
   * Checks if the annotations array does not contain either the {@link Required} or {@link
   * Optional} annotations. Loops around the array and check if it has either of too.
   *
   * @param annotations the array of annotations to check
   * @return true if the method does not contain either of both annotations
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
   * Annotation[], int)} basically this gives either {@link MultipleArgument} or {@link Argument} it
   * is check in the {@link #parseArgument(Class, Annotation[], int)} method if the parameter
   * contains the annotation {@link Multiple}
   *
   * @param parameter the parameter where the argument came from
   * @param position the position of the argument
   * @param multiple the annotation required to get an {@link MultipleArgument} it can be null
   * @param required whether the argument is required (if it has the annotation {@link Required} it
   *     is required)
   * @param name the name of the argument
   * @param description the description of the argument
   * @param suggestions the suggestions for the argument
   * @return the argument instance if multiple is tru it will be a {@link MultipleArgument} else
   *     just a {@link Argument}
   */
  @NonNull
  static Argument<?> getArgument(
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
      return new Argument<>(name, description, suggestions, parameter, required, position);
    }
  }
}

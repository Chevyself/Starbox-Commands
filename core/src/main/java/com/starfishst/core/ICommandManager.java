package com.starfishst.core;

import com.starfishst.core.annotations.Multiple;
import com.starfishst.core.annotations.Optional;
import com.starfishst.core.annotations.Required;
import com.starfishst.core.annotations.settings.Setting;
import com.starfishst.core.annotations.settings.Settings;
import com.starfishst.core.arguments.Argument;
import com.starfishst.core.arguments.ExtraArgument;
import com.starfishst.core.arguments.MultipleArgument;
import com.starfishst.core.arguments.type.ISimpleArgument;
import com.starfishst.core.exceptions.CommandRegistrationException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to register commands
 *
 * @param <C> the command class
 */
public interface ICommandManager<C extends ISimpleCommand<?>> {

  /**
   * Register a command in {@link ICommandManager}
   *
   * @param object the class instance of the command
   */
  void registerCommand(@NotNull Object object);

  /**
   * Parse the arguments of a command
   *
   * @param parameters the parameters of the command method
   * @param annotations the annotations of the parameters of the command method
   * @return the list of parsed {@link ISimpleArgument} empty if there's none
   */
  @NotNull
  default List<ISimpleArgument<?>> parseArguments(
      @NotNull final Class<?>[] parameters, @NotNull final Annotation[][] annotations) {
    List<ISimpleArgument<?>> arguments = new ArrayList<>();
    int position = 0;
    for (int i = 0; i < parameters.length; i++) {
      Annotation[] paramAnnotations = annotations[i];
      if (isEmpty(paramAnnotations)) {
        arguments.add(i, new ExtraArgument<>(parameters[i]));
      } else {
        arguments.add(i, this.parseArgument(parameters[i], annotations[i], position));
        position++;
      }
    }
    return arguments;
  }

  /**
   * Get a new instance of {@link ICommand}
   *
   * @param object the class instance of the command
   * @param method the method to run the command
   * @param isParent is the command a parent
   * @return the new instance of {@link ICommand}
   */
  @NotNull
  C parseCommand(@NotNull Object object, @NotNull Method method, boolean isParent);

  /**
   * Get a argument using the command annotations
   *
   * @param parameter the parameter of the command
   * @param annotations the annotations of the command
   * @param position the position of the parameter
   * @return the argument made with the annotations
   */
  @NotNull
  default Argument<?> parseArgument(Class<?> parameter, Annotation[] annotations, int position) {
    boolean multiple = hasAnnotation(annotations, Multiple.class);
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
   * Get the argument with the provided parameters
   *
   * @param parameter the parameter where the argument came from
   * @param position the position of the argument
   * @param multiple whether or not has the annotation {@link Multiple}
   * @param required whether the argument is required
   * @param name the name of the argument
   * @param description the description of the argument
   * @param suggestions the suggestions for the argument
   * @return the argument
   */
  @NotNull
  default Argument<?> getArgument(
      @NotNull Class<?> parameter,
      int position,
      boolean multiple,
      boolean required,
      @NotNull String name,
      @NotNull String description,
      @NotNull List<String> suggestions) {
    if (multiple) {
      return new MultipleArgument<>(name, description, suggestions, parameter, true, position);
    } else {
      return new Argument<>(name, description, suggestions, parameter, required, position);
    }
  }

  /**
   * Checks if an array of annotations has certain annotation
   *
   * @param annotations the array of annotations
   * @param search the annotation to match
   * @return true if the array was the annotation
   * @param <T> the type of annotation
   */
  default <T extends Annotation> boolean hasAnnotation(
      @NotNull Annotation[] annotations, @NotNull Class<T> search) {
    for (Annotation annotation : annotations) {
      if (search.isAssignableFrom(annotation.getClass())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the annotations of a method are empty
   *
   * @param annotations the annotations of the method
   * @return true if the annotations are empty
   */
  default boolean isEmpty(@NotNull Annotation[] annotations) {
    for (Annotation annotation : annotations) {
      if (annotation instanceof Required || annotation instanceof Optional) {
        return false;
      }
    }
    return true;
  }

  /**
   * Gets the settings for a command
   *
   * @param method the method of a command
   * @return the settings as a HashMap
   */
  @NotNull
  default HashMap<String, String> parseSettings(@NotNull Method method) {
    HashMap<String, String> settings = new HashMap<>();
    if (method.isAnnotationPresent(Settings.class)) {
      for (Setting setting : method.getAnnotation(Settings.class).settings()) {
        settings.put(setting.key(), setting.value());
      }
    }
    return settings;
  }
}

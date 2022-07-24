package me.googas.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;
import me.googas.commands.arguments.Argument;
import me.googas.commands.arguments.ArgumentBehaviour;
import me.googas.commands.context.StarboxCommandContext;

/**
 * This marks a parameter as optional. It can be thought in a way as a "Nullable".
 *
 * @see Argument
 *     <pre>
 * public void AMethod(@Required String name, @Free(suggestions = "20") int age) {
 *     // A required argument is name
 *     // An optional argument is age
 * }
 * </pre>
 *     <p>The Starbox commands uses Lombok which does not have a nullable annotation, you can use
 *     this annotation to tell your IDE that something is nullable in case you don't use another
 *     annotation dependency.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Free {

  /**
   * Returns the name of the argument.
   *
   * @return the name of the argument
   */
  @NonNull
  String name() default "No name provided";

  /**
   * Returns the description of the argument.
   *
   * @return the description of the argument
   */
  @NonNull
  String description() default "No description provided";

  /**
   * Returns an {@link java.lang.reflect.Array} of strings which represents the suggestions of the
   * argument. The default value of the optional argument will be the first entry of the {@link
   * java.lang.reflect.Array}, the other will be suggestions which can be sent to the user in case
   * the command is executed incorrectly.
   *
   * <p><b>Please note that certain arguments such as primitives must have a default value or else a
   * {@link NullPointerException} might be thrown</b>
   *
   * @see me.googas.commands.arguments.SingleArgument#getSuggestions(StarboxCommandContext)
   * @return the array of suggestions
   */
  @NonNull
  String[] suggestions() default {};

  /**
   * Returns the behaviour of the argument.
   *
   * @return the behaviour
   */
  ArgumentBehaviour behaviour() default ArgumentBehaviour.NORMAL;
}

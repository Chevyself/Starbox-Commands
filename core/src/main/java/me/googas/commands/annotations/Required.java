package me.googas.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;
import me.googas.commands.arguments.Argument;

/**
 * This marks a parameter as required. It can be think in a way as {@link NonNull}
 *
 * <p>You can check more about this annotation in {@link Argument}
 *
 * <pre>
 * public void AMethod(@Required String name, @Free(suggestions = "20") int age) {
 *     // A required argument is name
 *     // An optional argument is age
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Required {

  /**
   * Get the name of the argument.
   *
   * @return the name of the argument
   */
  @NonNull
  String name() default "No name provided";

  /**
   * Get the description of the argument.
   *
   * @return The description of the argument
   */
  @NonNull
  String description() default "No description provided";

  /**
   * Get the suggestions of the argument.
   *
   * @return the default value of the optional argument
   */
  @NonNull
  String[] suggestions() default {};
}

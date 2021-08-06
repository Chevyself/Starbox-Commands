package me.googas.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;
import me.googas.commands.arguments.Argument;

/**
 * This marks a parameter as optional. It can be think in a way as a Nullable.
 *
 * <p>You can check more about this annotation in {@link Argument}
 *
 * <pre>
 * public void AMethod(@Required String name, @Optional(suggestions = "20") int age) {
 *     // A required argument is name
 *     // An optional argument is age
 * }
 * </pre>
 *
 * <p>The Easy Commands project uses Lombok which does not have a nullable annotation, you can use
 * this annotation to tell your IDE that something is nullable in case you don't use another
 * annotation dependency.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Optional {

  /**
   * Get the name of the argument.
   *
   * @return The name of the argument as a string
   */
  @NonNull
  String name() default "No name provided";

  /**
   * Get the description of the argument.
   *
   * @return The description of the argument as a string
   */
  @NonNull
  String description() default "No description provided";

  /**
   * Get an array of suggestions that the user can input to execute the command correctly.
   *
   * @return The default value of the optional argument will be the first entry of the array the
   *     other will be suggestions which can be send to the user in case the command is executed
   *     incorrectly
   */
  @NonNull
  String[] suggestions() default {};
}

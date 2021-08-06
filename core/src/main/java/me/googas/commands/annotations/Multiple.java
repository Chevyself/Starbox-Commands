package me.googas.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.googas.commands.objects.JoinedStrings;

/**
 * Used for the representation of parameters that require multiple strings
 *
 * <p>An object that needs multiple stings can be something like an array. For example: The custom
 * object {@link JoinedStrings} which takes many strings to format a bigger {@link String}.
 *
 * <pre>
 * public void AMethod(@Multiple @Optional(suggestions = "Hello world") String message) {
 *     // A MultipleArgument is the message
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Multiple {

  /**
   * The minimum size that the argument accepts.
   *
   * @return the minimum size as an {@link Integer}
   */
  int min() default 1;

  /**
   * The maximum size that the argument accepts or -1 for infinite.
   *
   * @return the maximum size as an {@link Integer}
   */
  int max() default -1;
}

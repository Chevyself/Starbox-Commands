package me.googas.commands.annotations;

import me.googas.commands.objects.JoinedStrings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used for the representation of parameters that require multiple strings
 *
 * <p>An object that needs multiple stings can be something like an array. For example: The custom
 * object {@link JoinedStrings}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Multiple {

  /**
   * The minimum size that the argument accepts
   *
   * @return the minimum size
   */
  int min() default 1;

  /**
   * The maximum size that the argument accepts. -1 for infinite
   *
   * @return the maximum size
   */
  int max() default -1;
}

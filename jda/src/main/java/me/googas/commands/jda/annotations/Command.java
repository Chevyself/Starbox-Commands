package me.googas.commands.jda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;
import me.googas.commands.flags.Flag;
import me.googas.commands.jda.middleware.JdaMiddleware;
import me.googas.commands.time.annotations.TimeAmount;

/**
 * When you include this annotation into a {@link java.lang.reflect.Method} and invoke {@link
 * me.googas.commands.jda.CommandManager#parseCommands(Object)} it tells it that the {@link
 * java.lang.reflect.Method} must create a {@link me.googas.commands.jda.AnnotatedCommand}
 *
 * @see me.googas.commands.jda.CommandManager#parseCommands(Object)
 * @see me.googas.commands.jda.AnnotatedCommand
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

  /**
   * The aliases that can be used for the command.
   *
   * @return an array of strings representing the aliases
   */
  @NonNull
  String[] aliases();

  /**
   * The description of the command.
   *
   * @return the description as a string
   */
  @NonNull
  String description() default "No description provided";

  /**
   * The amount of time until the command can be executed again.
   *
   * @return the time amount
   */
  @NonNull
  TimeAmount cooldown() default @TimeAmount();

  /**
   * Get the options/flags to apply in this command.
   *
   * @return the array of flags
   */
  @NonNull
  Flag[] options() default {};

  /**
   * Get the middleware classes that should be included in the execution.
   *
   * @return the array of classes
   */
  @NonNull
  Class<? extends JdaMiddleware>[] include() default {};

  /**
   * Get the global middleware classes that should be excluded from the command execution.
   *
   * @return the array of classes
   */
  @NonNull
  Class<? extends JdaMiddleware>[] exclude() default {};

  /**
   * A map of custom settings for the command.
   *
   * <p>This can be used to create your own {@link JdaMiddleware} and add the things you need to
   * this annotation as it was the case with the {@link
   * me.googas.commands.jda.middleware.PermissionMiddleware}
   *
   * @return the map as an array of entries
   */
  @NonNull
  Entry[] map() default {};

  /**
   * Whether to exclude the result of this command from being deleted when it is successful.
   *
   * @return true if the result should be excluded
   */
  @Deprecated
  boolean excluded() default false;
}

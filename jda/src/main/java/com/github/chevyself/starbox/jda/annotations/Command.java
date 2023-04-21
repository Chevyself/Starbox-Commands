package com.github.chevyself.starbox.jda.annotations;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.flags.Flag;
import com.github.chevyself.starbox.jda.AnnotatedCommand;
import com.github.chevyself.starbox.jda.CommandManager;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.middleware.JdaMiddleware;
import com.github.chevyself.starbox.jda.middleware.PermissionMiddleware;
import com.github.chevyself.starbox.time.annotations.TimeAmount;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;

/**
 * When you include this annotation into a {@link java.lang.reflect.Method} and invoke {@link
 * CommandManager#parseCommands(Object)} it tells it that the {@link java.lang.reflect.Method} must
 * create a {@link AnnotatedCommand}
 *
 * @see CommandManager#parseCommands(Object)
 * @see AnnotatedCommand
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
  Class<? extends Middleware<CommandContext>>[] include() default {};

  /**
   * Get the global middleware classes that should be excluded from the command execution.
   *
   * @return the array of classes
   */
  @NonNull
  Class<? extends Middleware<CommandContext>>[] exclude() default {};

  /**
   * A map of custom settings for the command.
   *
   * <p>This can be used to create your own {@link JdaMiddleware} and add the things you need to
   * this annotation as it was the case with the {@link PermissionMiddleware}
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

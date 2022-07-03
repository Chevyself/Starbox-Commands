package me.googas.commands.bungee.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;
import me.googas.commands.Middleware;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.flags.Flag;

/**
 * When you include this annotation into a {@link java.lang.reflect.Method} and invoke {@link
 * me.googas.commands.bungee.CommandManager#parseCommands(Object)} it tells it that the {@link
 * java.lang.reflect.Method} must create a {@link me.googas.commands.bungee.AnnotatedCommand}
 *
 * @see me.googas.commands.bungee.CommandManager#parseCommands(Object)
 * @see me.googas.commands.bungee.AnnotatedCommand
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

  /**
   * The aliases of the command.
   *
   * @return an array of aliases
   */
  @NonNull
  String[] aliases();

  /**
   * The permission to use the command.
   *
   * @return the permission node
   */
  @NonNull
  String permission() default "";

  /**
   * TODO documentation.
   *
   * @return whether to execute the command async
   */
  boolean async() default false;

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
   * Get the cooldown that should be applied to this command.
   *
   * @return the annotation
   */
  @NonNull
  Cooldown cooldown() default @Cooldown;
}

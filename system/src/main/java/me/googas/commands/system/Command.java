package me.googas.commands.system;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;
import me.googas.commands.Middleware;
import me.googas.commands.flags.Flag;
import me.googas.commands.system.context.CommandContext;
import me.googas.commands.time.annotations.TimeAmount;

/**
 * When you include this annotation into a {@link java.lang.reflect.Method} and invoke {@link
 * me.googas.commands.system.CommandManager#parseCommands(Object)} it tells it that the {@link
 * java.lang.reflect.Method} must create a {@link me.googas.commands.system.ReflectSystemCommand}
 *
 * @see me.googas.commands.system.CommandManager#parseCommands(Object)
 * @see me.googas.commands.system.ReflectSystemCommand
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

  /**
   * Get the array of aliases to match the command in {@link CommandManager#getCommand(String)}.,
   *
   * @return the array of aliases
   */
  @NonNull
  String[] aliases();

  /**
   * Get the options/flags to apply in this command.
   *
   * @return the array of flags
   */
  @NonNull
  Flag[] options() default {};

  /**
   * The amount of time until the command can be executed again.
   *
   * @return the time amount
   */
  @NonNull
  TimeAmount cooldown() default @TimeAmount();

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
}

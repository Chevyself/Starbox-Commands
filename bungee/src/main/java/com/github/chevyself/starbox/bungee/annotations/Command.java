package com.github.chevyself.starbox.bungee.annotations;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.bungee.AnnotatedCommand;
import com.github.chevyself.starbox.bungee.CommandManager;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.flags.Flag;
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
@Target({ElementType.METHOD, ElementType.TYPE})
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
   * Whether the command should be executed async. To know more about Asynchronization check <a
   * href="https://bukkit.fandom.com/wiki/Scheduler_Programming">Bukkit wiki</a>
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

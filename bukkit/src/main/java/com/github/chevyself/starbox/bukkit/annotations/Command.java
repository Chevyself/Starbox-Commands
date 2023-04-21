package com.github.chevyself.starbox.bukkit.annotations;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.bukkit.AnnotatedCommand;
import com.github.chevyself.starbox.bukkit.CommandManager;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.middleware.PermissionMiddleware;
import com.github.chevyself.starbox.flags.Flag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;

/**
 * This marks a {@link java.lang.reflect.Method} as a {@link AnnotatedCommand}.
 *
 * <p>When you include this annotation into a {@link java.lang.reflect.Method} and invoke {@link
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
   * Get the aliases of the command.
   *
   * @return the aliases of the command
   * @see AnnotatedCommand#getAliases()
   */
  String[] aliases() default {};

  /**
   * Get a brief description of the command.
   *
   * @return the description of the command
   * @see AnnotatedCommand
   */
  String description() default "No description provided";

  /**
   * Get the permission node of the command.
   *
   * @return the permission node of the command
   * @see PermissionMiddleware
   */
  String permission() default "";

  /**
   * Whether the command should be executed async. To know more about Asynchronization check <a
   * href="https://bukkit.fandom.com/wiki/Scheduler_Programming">Bukkit wiki</a>
   *
   * @return true if the command has to run the command asynchronously
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

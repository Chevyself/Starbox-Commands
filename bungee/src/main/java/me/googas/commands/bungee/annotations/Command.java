package me.googas.commands.bungee.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
   * The aliases of the command
   *
   * @return an array of aliases
   */
  String[] aliases();

  /**
   * The permission to use the command
   *
   * @return the permission node
   */
  String permission() default "";

  /**
   * TODO documentation
   *
   * @return whether to execute the command async
   */
  boolean async() default false;
}

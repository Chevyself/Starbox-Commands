package me.googas.commands.system;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;

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
   * Get the array of aliases to match the command in {@link CommandManager#getCommand(String)}
   *
   * @return the array of aliases
   */
  @NonNull
  String[] aliases();
}

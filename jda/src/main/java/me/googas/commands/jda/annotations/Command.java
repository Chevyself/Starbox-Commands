package me.googas.commands.jda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;
import me.googas.starbox.annotations.TimeAmount;
import net.dv8tion.jda.api.Permission;

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
   * The aliases that can be used for the command
   *
   * @return an array of strings representing the aliases
   */
  @NonNull
  String[] aliases();

  /**
   * The description of the command
   *
   * @return the description as a string
   */
  @NonNull
  String description() default "No description provided";

  /**
   * Get the permission as a string node. If this is left empty then it will use the {@link}
   *
   * @return the string permission
   */
  @NonNull
  String node() default "";

  /**
   * Get the permission as a discord permission. IF this is left in {@link Permission#UNKNOWN} and
   * the permission node is empty then the command will not have permission
   *
   * @return the discord permission
   */
  @NonNull
  Permission permission() default Permission.UNKNOWN;

  @NonNull
  TimeAmount cooldown() default @TimeAmount();

  boolean excluded() default false;
}

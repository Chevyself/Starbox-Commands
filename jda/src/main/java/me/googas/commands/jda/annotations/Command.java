package me.googas.commands.jda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;
import me.googas.commands.jda.cooldown.CooldownBehaviour;
import me.googas.commands.time.annotations.TimeAmount;
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
   * Whether to exclude the result of this command from being deleted when it is successful.
   *
   * @return true if the result should be excluded
   */
  boolean excluded() default false;

  /**
   * Get how should cooldown behave.
   *
   * @return how will the cooldown check if a user/member is allowed to use a command
   */
  @NonNull
  CooldownBehaviour behaviour() default CooldownBehaviour.USER;

  /**
   * Get the permission that the user/member needs to be allowed to use the command.
   *
   * @return the permission annotation
   */
  @NonNull
  Perm permission() default @Perm;

  /**
   * Get the permission that the user/member needs to skip cooldown.
   *
   * @return the permission annotation
   */
  @NonNull
  Perm cooldownPerm() default @Perm(value = Permission.ADMINISTRATOR, node = "*.cooldown");
}

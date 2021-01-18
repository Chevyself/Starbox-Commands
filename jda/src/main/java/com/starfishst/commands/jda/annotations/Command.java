package com.starfishst.commands.jda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;
import net.dv8tion.jda.api.Permission;

/** This annotation is used to tell what method is a command inside a class */
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
  String node() default "";

  /**
   * Get the permission as a discord permission. IF this is left in {@link Permission#UNKNOWN} and
   * the permission node is empty then the command will not have permission
   *
   * @return the discord permission
   */
  Permission permission() default Permission.UNKNOWN;

  /**
   * The time for a user to use the command again
   *
   * @return the time for the user to use the command again as string
   */
  @NonNull
  String time() default "none";
}

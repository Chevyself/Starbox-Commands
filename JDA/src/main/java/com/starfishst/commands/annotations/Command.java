package com.starfishst.commands.annotations;

import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** This annotation is used to tell what method is a command inside a class */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

  /**
   * The aliases that can be used for the command
   *
   * @return an array of strings representing the aliases
   */
  @NotNull
  String[] aliases();

  /**
   * The description of the command
   *
   * @return the description as a string
   */
  @NotNull
  String description() default "No description provided";

  /**
   * The permission of the command to be used. If it is kept as {@link Permission#UNKNOWN} it wont
   * have one
   *
   * @return the permission of the command
   */
  @NotNull
  Permission permission() default Permission.UNKNOWN;

  /**
   * The time for a user to use the command again
   *
   * @return the time for the user to use the command again as string
   */
  @NotNull
  String time() default "none";
}

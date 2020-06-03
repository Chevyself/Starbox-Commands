package com.starfishst.bukkit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** The annotation that show the command manager that a method is a command to parse */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

  /**
   * The aliases of the command
   *
   * @return the aliases of the command
   */
  String[] aliases() default {};

  /**
   * Get a brief description of the command
   *
   * @return the description of the command
   */
  String description() default "No description provided";

  /**
   * Get the permission node of the command
   *
   * @return the permission node of the command
   */
  String permission() default "";
}

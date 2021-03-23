package me.googas.commands.bungee.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** This allows the command manager to identify a methods as a command */
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
   * @return
   */
  boolean async() default false;
}

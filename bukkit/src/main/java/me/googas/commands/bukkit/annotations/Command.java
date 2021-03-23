package me.googas.commands.bukkit.annotations;

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

  /**
   * Whether the command should be executed async. To know more about Asynchronization check <a
   * href="https://bukkit.fandom.com/wiki/Scheduler_Programming">Bukkit wiki</a>
   *
   * @return true if the command has to run the command asynchronously
   */
  boolean async() default false;
}

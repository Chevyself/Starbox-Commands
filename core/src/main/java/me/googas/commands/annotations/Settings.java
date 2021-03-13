package me.googas.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.NonNull;

/** The settings for a command */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Settings {

  /**
   * Settings are now like program arguments. It is a single string which is then parsed by {@link
   * me.googas.commons.ProgramArguments}
   *
   * @return the string value of the command settings
   */
  @NonNull
  String value() default "";
}

package com.starfishst.core.annotations.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** The settings for a command */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Settings {

  /**
   * Get the settings which were annotated to the command method
   *
   * @return the settings array
   */
  Setting[] settings();
}

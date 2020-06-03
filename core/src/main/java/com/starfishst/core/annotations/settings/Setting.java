package com.starfishst.core.annotations.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Settings for a command */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Setting {
  /**
   * Get the key of the setting
   *
   * @return the key
   */
  String key();

  /**
   * Get the value of the setting
   *
   * @return the value
   */
  String value();
}

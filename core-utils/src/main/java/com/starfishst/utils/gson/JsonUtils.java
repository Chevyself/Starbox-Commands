package com.starfishst.utils.gson;

import org.jetbrains.annotations.NotNull;

/** Some utilities for JSON */
public class JsonUtils {

  /**
   * Checks if a string is json
   *
   * @param string the string to check
   * @return true if the string is json
   */
  public static boolean isJson(@NotNull String string) {
    return string.startsWith("{") && string.endsWith("}")
        || string.startsWith("[") && string.endsWith("]");
  }
}

package com.starfishst.utils.gson;

import org.jetbrains.annotations.NotNull;

/** Static utilities for json */
public class JsonUtils {

  /**
   * Checks if a string is json. This method will return true if the string starts with "[" or "{"
   * and ends with "]" or "}"
   *
   * @param string the string to check
   * @return true if the string is json
   */
  public static boolean isJson(@NotNull String string) {
    return string.startsWith("{") && string.endsWith("}")
        || string.startsWith("[") && string.endsWith("]");
  }
}

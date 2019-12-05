package com.starfishst.core.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Utils for strings */
public class Strings {

  @NotNull private static final StringBuilder builder = new StringBuilder();

  /**
   * Get a clear instance of {@link StringBuilder}
   *
   * @return the clear instance (length 0)
   */
  public static StringBuilder getBuilder() {
    Strings.builder.setLength(0);
    return Strings.builder;
  }

  /**
   * Build a message which has place holders
   *
   * @param message the message
   * @param strings the place holders
   * @return the built message
   */
  @NotNull
  public static String buildMessage(@Nullable String message, Object... strings) {
    if (message != null) {
      for (int i = 0; i < strings.length; i++) {
        message =
            message.replace("{" + i + "}", strings[i] == null ? "Null" : strings[i].toString());
      }
    } else {
      message = "Null";
    }
    return message;
  }

  /**
   * Builds a String from an array.
   *
   * @param strings the string array
   * @return a brand new string
   */
  public static String fromArray(@NotNull String[] strings) {
    StringBuilder builder = Strings.getBuilder();

    for (String string : strings) {
      builder.append(string).append(" ");
    }
    if (builder.length() >= 1) {
      builder.deleteCharAt(builder.length() - 1);
    }
    return builder.toString();
  }
}

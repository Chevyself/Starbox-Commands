package com.starfishst.core.exceptions.type;

import com.starfishst.core.utils.Strings;
import org.jetbrains.annotations.NotNull;

/** Represent a simple exception to easily create messages */
public class SimpleException extends Exception {

  /**
   * Throw the exception using a message with place holders
   *
   * @param message the message
   * @param objects the place holders replacers
   */
  protected SimpleException(@NotNull String message, @NotNull Object... objects) {
    super(Strings.buildMessage(message, objects));
  }
}

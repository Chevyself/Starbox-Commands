package com.starfishst.core.exceptions.type;

import com.starfishst.core.utils.Strings;
import org.jetbrains.annotations.NotNull;

public class SimpleRuntimeException extends RuntimeException {

  /**
   * Throw the exception using a message with place holders
   *
   * @param message the message
   * @param objects the place holders replacers
   */
  protected SimpleRuntimeException(@NotNull String message, @NotNull Object... objects) {
    super(Strings.buildMessage(message, objects));
  }
}

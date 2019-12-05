package com.starfishst.core.exceptions;

import com.starfishst.core.exceptions.type.SimpleRuntimeException;
import org.jetbrains.annotations.NotNull;

/** Thrown when there's been an issue trying to register a command */
public class CommandRegistrationException extends SimpleRuntimeException {

  /**
   * Throw the exception using a message with place holders
   *
   * @param message the message
   * @param objects the place holders replacers
   */
  public CommandRegistrationException(@NotNull String message, @NotNull Object... objects) {
    super(message, objects);
  }
}

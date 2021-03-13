package me.googas.commands.exceptions;

import me.googas.commands.exceptions.type.SimpleRuntimeException;
import lombok.NonNull;

/** Thrown when there's been an issue trying to register a command */
public class CommandRegistrationException extends SimpleRuntimeException {

  /**
   * Throw the exception when a command could not be registered
   *
   * @param message the message
   */
  public CommandRegistrationException(@NonNull String message) {
    super(message);
  }
}

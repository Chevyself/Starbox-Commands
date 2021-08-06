package me.googas.commands.exceptions;

import lombok.NonNull;
import me.googas.commands.StarboxCommand;
import me.googas.commands.StarboxCommandManager;
import me.googas.commands.exceptions.type.StarboxRuntimeException;

/**
 * This command is thrown by the {@link StarboxCommandManager} when it cannot parseAndRegister an
 * {@link StarboxCommand}.
 */
public class CommandRegistrationException extends StarboxRuntimeException {

  /**
   * Create a simple exception with a simple message.
   *
   * @param message the message with the cause of the exception
   */
  public CommandRegistrationException(@NonNull String message) {
    super(message);
  }
}

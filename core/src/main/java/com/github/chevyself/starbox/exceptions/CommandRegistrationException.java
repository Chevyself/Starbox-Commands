package com.github.chevyself.starbox.exceptions;

import com.github.chevyself.starbox.StarboxCommand;
import com.github.chevyself.starbox.StarboxCommandManager;
import com.github.chevyself.starbox.exceptions.type.StarboxRuntimeException;
import lombok.NonNull;

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

  /**
   * Create an exception with a message and a cause.
   *
   * @param message the message with the cause of the exception
   * @param e the cause of the exception
   */
  public CommandRegistrationException(String message, ReflectiveOperationException e) {
    super(message, e);
  }
}

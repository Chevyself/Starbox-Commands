package com.github.chevyself.starbox.exceptions;

import lombok.NonNull;

/**
 * Exception thrown when a {@link com.github.chevyself.starbox.middleware.Middleware} could not be
 * parsed.
 */
public class MiddlewareParsingException extends CommandRegistrationException {

  /**
   * Create the exception with a message.
   *
   * @param message the message of the exception
   */
  public MiddlewareParsingException(String message) {
    super(message);
  }

  /**
   * Create the exception with a message and a cause.
   *
   * @param message the message of the exception
   * @param cause the cause of the exception
   */
  public MiddlewareParsingException(String message, @NonNull Throwable cause) {
    super(message, cause);
  }
}

package com.github.chevyself.starbox.exceptions;

import lombok.NonNull;

public class MiddlewareParsingException extends CommandRegistrationException {

  public MiddlewareParsingException(String message) {
    super(message);
  }

  public MiddlewareParsingException(String message, @NonNull Throwable cause) {
    super(message, cause);
  }
}

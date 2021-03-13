package me.googas.commands.exceptions.type;

import lombok.NonNull;

/** A runtime exception that can be handled in commands */
public class SimpleRuntimeException extends RuntimeException {

  public SimpleRuntimeException() {}

  public SimpleRuntimeException(@NonNull String message) {
    super(message);
  }

  public SimpleRuntimeException(String message, @NonNull Throwable cause) {
    super(message, cause);
  }

  public SimpleRuntimeException(@NonNull Throwable cause) {
    super(cause);
  }
}

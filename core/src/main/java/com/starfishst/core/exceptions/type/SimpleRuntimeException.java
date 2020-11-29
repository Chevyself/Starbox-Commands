package com.starfishst.core.exceptions.type;

import lombok.NonNull;

/** A runtime exception that can be handled in commands */
public class SimpleRuntimeException extends RuntimeException {

  /**
   * Throw a simple runtime exception
   *
   * @param message the message
   */
  public SimpleRuntimeException(@NonNull String message) {
    super(message);
  }

  /**
   * Throw a simple runtime exception
   *
   * @param message the message
   * @param cause the cause of the exception
   */
  public SimpleRuntimeException(@NonNull String message, @NonNull Throwable cause) {
    super(message, cause);
  }

  /**
   * Throw a simple runtime exception
   *
   * @param cause the cause of the exception
   */
  public SimpleRuntimeException(@NonNull Throwable cause) {
    super(cause);
  }
}

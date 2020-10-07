package com.starfishst.core.exceptions.type;

import org.jetbrains.annotations.NotNull;

/** A runtime exception that can be handled in commands */
public class SimpleRuntimeException extends RuntimeException {

  /**
   * Throw a simple runtime exception
   *
   * @param message the message
   */
  public SimpleRuntimeException(@NotNull String message) {
    super(message);
  }

  /**
   * Throw a simple runtime exception
   *
   * @param message the message
   * @param cause the cause of the exception
   */
  public SimpleRuntimeException(@NotNull String message, @NotNull Throwable cause) {
    super(message, cause);
  }

  /**
   * Throw a simple runtime exception
   *
   * @param cause the cause of the exception
   */
  public SimpleRuntimeException(@NotNull Throwable cause) {
    super(cause);
  }
}

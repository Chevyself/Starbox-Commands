package com.starfishst.core.exceptions.type;

import lombok.NonNull;

/** Represent a simple exception to easily create messages */
public class SimpleException extends Exception {

  /**
   * Throw a simple exception
   *
   * @param message the message
   */
  public SimpleException(@NonNull String message) {
    super(message);
  }

  /**
   * Throw a simple exception
   *
   * @param message the message
   * @param cause the cause of the exception
   */
  public SimpleException(@NonNull String message, @NonNull Throwable cause) {
    super(message, cause);
  }

  /**
   * Throw a simple exception
   *
   * @param cause the cause of the exception
   */
  public SimpleException(@NonNull Throwable cause) {
    super(cause);
  }
}

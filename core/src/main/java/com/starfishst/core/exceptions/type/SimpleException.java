package com.starfishst.core.exceptions.type;

import org.jetbrains.annotations.NotNull;

/** Represent a simple exception to easily create messages */
public class SimpleException extends Exception {

  /**
   * Throw a simple exception
   *
   * @param message the message
   */
  public SimpleException(@NotNull String message) {
    super(message);
  }

  /**
   * Throw a simple exception
   *
   * @param message the message
   * @param cause the cause of the exception
   */
  public SimpleException(String message, Throwable cause) {
    super(message, cause);
  }
}

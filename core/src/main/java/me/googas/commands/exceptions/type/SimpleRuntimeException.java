package me.googas.commands.exceptions.type;

import lombok.NonNull;

/**
 * This is the same as a {@link SimpleException} but it implements {@link RuntimeException} as it
 * might be a programmer exception
 */
public class SimpleRuntimeException extends RuntimeException {

  /** Create an exception with no message */
  public SimpleRuntimeException() {}

  /**
   * Create a simple exception with a simple message
   *
   * @param message the message with the cause of the exception
   */
  public SimpleRuntimeException(@NonNull String message) {
    super(message);
  }

  /**
   * Create a simple exception with a message detailing the cause and the cause of the exception
   *
   * @param message the message to detail the cause of the exception
   * @param cause the other cause of this exception
   */
  public SimpleRuntimeException(String message, @NonNull Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a simple exception that was caused by another exception
   *
   * @param cause the cause of the exception
   */
  public SimpleRuntimeException(@NonNull Throwable cause) {
    super(cause);
  }
}

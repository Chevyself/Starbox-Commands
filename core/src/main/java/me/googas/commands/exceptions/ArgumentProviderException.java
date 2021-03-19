package me.googas.commands.exceptions;

import lombok.NonNull;
import me.googas.commands.exceptions.type.SimpleException;
import me.googas.commands.providers.type.EasyArgumentProvider;

/**
 * This exception is thrown by {@link EasyArgumentProvider} when they cannot provide the object as
 * they are required to
 */
public class ArgumentProviderException extends SimpleException {

  /** Create an exception with no message */
  public ArgumentProviderException() {}

  /**
   * Create a simple exception with a simple message
   *
   * @param message the message with the cause of the exception
   */
  public ArgumentProviderException(@NonNull String message) {
    super(message);
  }

  /**
   * Create a simple exception with a message detailing the cause and the cause of the exception
   *
   * @param message the message to detail the cause of the exception
   * @param cause the other cause of this exception
   */
  public ArgumentProviderException(String message, @NonNull Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a simple exception that was caused by another exception
   *
   * @param cause the cause of the exception
   */
  public ArgumentProviderException(@NonNull Throwable cause) {
    super(cause);
  }
}

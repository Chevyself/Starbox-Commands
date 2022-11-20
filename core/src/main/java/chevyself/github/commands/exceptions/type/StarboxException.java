package chevyself.github.commands.exceptions.type;

import lombok.NonNull;

/** Represent a simple exception to easily create messages. */
public class StarboxException extends Exception {

  /** Create an exception with no message. */
  public StarboxException() {
    super();
  }

  /**
   * Create a simple exception with a simple message.
   *
   * @param message the message with the cause of the exception
   */
  public StarboxException(@NonNull String message) {
    super(message);
  }

  /**
   * Create a simple exception with a message detailing the cause and the cause of the exception.
   *
   * @param message the message to detail the cause of the exception
   * @param cause the other cause of this exception
   */
  public StarboxException(String message, @NonNull Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a simple exception that was caused by another exception.
   *
   * @param cause the cause of the exception
   */
  public StarboxException(@NonNull Throwable cause) {
    super(cause);
  }
}

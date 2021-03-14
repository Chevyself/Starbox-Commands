package me.googas.commands.exceptions;

import lombok.NonNull;
import me.googas.commands.exceptions.type.SimpleException;

/**
 * This exception is thrown when an argument is going to return null (they must return the object)
 */
public class ArgumentProviderException extends SimpleException {

  public ArgumentProviderException() {}

  public ArgumentProviderException(@NonNull String message) {
    super(message);
  }

  public ArgumentProviderException(String message, @NonNull Throwable cause) {
    super(message, cause);
  }

  public ArgumentProviderException(@NonNull Throwable cause) {
    super(cause);
  }
}

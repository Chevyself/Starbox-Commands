package me.googas.commands.exceptions;

import lombok.NonNull;
import me.googas.commands.exceptions.type.SimpleException;

/** An exception that is thrown when the user does not input an argument that is required */
public class MissingArgumentException extends SimpleException {

  public MissingArgumentException() {}

  public MissingArgumentException(@NonNull String message) {
    super(message);
  }

  public MissingArgumentException(String message, @NonNull Throwable cause) {
    super(message, cause);
  }

  public MissingArgumentException(@NonNull Throwable cause) {
    super(cause);
  }
}

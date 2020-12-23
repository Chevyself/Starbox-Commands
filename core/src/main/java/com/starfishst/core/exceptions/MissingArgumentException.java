package com.starfishst.core.exceptions;

import com.starfishst.core.exceptions.type.SimpleException;
import lombok.NonNull;

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

package com.starfishst.core.exceptions;

import com.starfishst.core.exceptions.type.SimpleException;
import lombok.NonNull;

/** An exception that is thrown when the user does not input an argument that is required */
public class MissingArgumentException extends SimpleException {
  /**
   * Throw a simple exception
   *
   * @param message the message
   */
  public MissingArgumentException(@NonNull String message) {
    super(message);
  }
}

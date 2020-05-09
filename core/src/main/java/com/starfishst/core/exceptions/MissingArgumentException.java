package com.starfishst.core.exceptions;

import com.starfishst.core.exceptions.type.SimpleException;
import org.jetbrains.annotations.NotNull;

/** An exception that is thrown when the user does not input an argument that is required */
public class MissingArgumentException extends SimpleException {
  /**
   * Throw a simple exception
   *
   * @param message the message
   */
  public MissingArgumentException(@NotNull String message) {
    super(message);
  }
}

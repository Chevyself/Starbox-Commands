package com.starfishst.core.exceptions;

import com.starfishst.core.exceptions.type.SimpleException;
import org.jetbrains.annotations.NotNull;

/**
 * This exception is thrown when an argument is going to return null (they must return the object)
 */
public class ArgumentProviderException extends SimpleException {

  /**
   * Throw the exception using a message with place holders
   *
   * @param message the message
   * @param objects the place holders replacers
   */
  public ArgumentProviderException(@NotNull String message, @NotNull Object... objects) {
    super(message, objects);
  }
}

package com.starfishst.core.utils.sockets.exception;

import com.starfishst.core.exceptions.type.SimpleException;
import org.jetbrains.annotations.NotNull;

/** An exception thrown by a socket */
public class SocketException extends SimpleException {
  /**
   * Throw a simple exception
   *
   * @param message the message
   */
  public SocketException(@NotNull String message) {
    super(message);
  }

  /**
   * Throw a simple exception
   *
   * @param message the message
   * @param cause the cause of the exception
   */
  public SocketException(String message, Throwable cause) {
    super(message, cause);
  }
}

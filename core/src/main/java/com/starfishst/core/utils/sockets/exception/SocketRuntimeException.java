package com.starfishst.core.utils.sockets.exception;

import com.starfishst.core.exceptions.type.SimpleRuntimeException;
import org.jetbrains.annotations.NotNull;

/** Thrown when something with sockets is wrong coded */
public class SocketRuntimeException extends SimpleRuntimeException {

  /**
   * Throw a simple runtime exception
   *
   * @param message the message
   */
  public SocketRuntimeException(@NotNull String message) {
    super(message);
  }

  /**
   * Throw a simple runtime exception
   *
   * @param message the message
   * @param cause the cause of the exception
   */
  public SocketRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}

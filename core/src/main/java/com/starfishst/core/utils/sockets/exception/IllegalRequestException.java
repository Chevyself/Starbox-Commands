package com.starfishst.core.utils.sockets.exception;

import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import org.jetbrains.annotations.NotNull;

/** Thrown when a request does not have a legal method */
public class IllegalRequestException extends SocketException {

  /**
   * Throw a simple exception
   *
   * @param request the illegal request
   */
  public IllegalRequestException(@NotNull SocketRequest request) {
    super(request + " does not have a method!");
  }

  /**
   * Throw a simple exception
   *
   * @param message the message
   * @param cause the cause of the exception
   */
  public IllegalRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}

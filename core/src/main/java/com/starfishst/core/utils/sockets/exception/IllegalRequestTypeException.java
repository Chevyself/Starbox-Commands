package com.starfishst.core.utils.sockets.exception;

/** Thrown when a request contains an illegal type */
public class IllegalRequestTypeException extends SocketRuntimeException {

  /**
   * Throw a simple runtime exception
   *
   * @param message the message
   * @param cause the cause of the exception
   */
  public IllegalRequestTypeException(String message, Throwable cause) {
    super(message, cause);
  }
}

package com.starfishst.core.utils.sockets.exception;

import org.jetbrains.annotations.NotNull;

/** Thrown when a handler for certain method of a request is not found */
public class RequestHandlerNotFoundException extends SocketException {

  /**
   * Throw it
   *
   * @param method the method that was requested
   */
  public RequestHandlerNotFoundException(@NotNull String method) {
    super("Handler matching the method " + method + " was not found!");
  }
}

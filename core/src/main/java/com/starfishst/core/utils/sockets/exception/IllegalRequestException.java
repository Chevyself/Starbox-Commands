package com.starfishst.core.utils.sockets.exception;

import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import org.jetbrains.annotations.NotNull;

public class IllegalRequestException extends SocketException {

  /**
   * Throw a simple exception
   *
   * @param request the illegal request
   */
  public IllegalRequestException(@NotNull SocketRequest request) {
    super(request + " does not have a method!");
  }
}

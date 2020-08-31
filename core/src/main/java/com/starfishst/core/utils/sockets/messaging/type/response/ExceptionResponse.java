package com.starfishst.core.utils.sockets.messaging.type.response;

import com.starfishst.core.utils.maps.Maps;
import com.starfishst.core.utils.sockets.messaging.SocketResponse;
import org.jetbrains.annotations.NotNull;

/** A response send when the request ended with an exception */
public class ExceptionResponse extends SocketResponse {

  /**
   * Create the response
   *
   * @param message the message to send in the response
   */
  public ExceptionResponse(@NotNull String message) {
    super(Maps.singleton("exception-message", message));
  }
}

package com.starfishst.core.utils.sockets.messaging.handlers.type;

import com.starfishst.core.utils.sockets.messaging.IMessenger;
import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import com.starfishst.core.utils.sockets.messaging.SocketResponse;
import com.starfishst.core.utils.sockets.messaging.handlers.RequestHandler;
import com.starfishst.core.utils.sockets.messaging.type.response.ExceptionResponse;
import org.jetbrains.annotations.NotNull;

/** Handles request of empty methods */
public class EmptyHandler implements RequestHandler {
  @Override
  public @NotNull String method() {
    return "empty";
  }

  @Override
  public @NotNull SocketResponse getResponse(
      @NotNull SocketRequest request, @NotNull IMessenger messenger) {
    return new ExceptionResponse(request + " does not have a method!");
  }
}

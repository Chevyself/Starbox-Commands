package com.starfishst.core.utils.sockets.messaging.handlers.type;

import com.starfishst.core.utils.maps.Maps;
import com.starfishst.core.utils.sockets.messaging.IMessenger;
import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import com.starfishst.core.utils.sockets.messaging.SocketResponse;
import com.starfishst.core.utils.sockets.messaging.handlers.RequestHandler;
import org.jetbrains.annotations.NotNull;

/** Handles the heartbeat of the other socket */
public class HeartbeatHandler implements RequestHandler {
  /**
   * The method that this handler manages
   *
   * @return the method as string
   */
  @Override
  public @NotNull String method() {
    return "heartbeat";
  }

  /**
   * Get the handled response for the request
   *
   * @param request asking for a response
   * @param messenger the messenger that caught the request
   * @return the response
   */
  @Override
  public @NotNull SocketResponse getResponse(
      @NotNull SocketRequest request, @NotNull IMessenger messenger) {
    return new SocketResponse(Maps.singleton("beating", "true"));
  }
}

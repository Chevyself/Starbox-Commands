package com.starfishst.core.utils.sockets.messaging.handlers;

import com.starfishst.core.utils.sockets.messaging.IMessenger;
import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import com.starfishst.core.utils.sockets.messaging.SocketResponse;
import org.jetbrains.annotations.NotNull;

/** A handler for requests between sockets */
public interface RequestHandler {

  /**
   * The method that this handler manages
   *
   * @return the method as string
   */
  @NotNull
  String method();

  /**
   * Get the handled response for the request
   *
   * @param request asking for a response
   * @param messenger the messenger that caught the request
   * @return the response
   */
  @NotNull
  SocketResponse getResponse(@NotNull SocketRequest request, @NotNull IMessenger messenger);
}

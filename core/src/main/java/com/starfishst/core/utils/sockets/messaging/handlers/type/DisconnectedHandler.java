package com.starfishst.core.utils.sockets.messaging.handlers.type;

import com.starfishst.core.utils.sockets.client.Client;
import com.starfishst.core.utils.sockets.messaging.IMessenger;
import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import com.starfishst.core.utils.sockets.messaging.SocketResponse;
import com.starfishst.core.utils.sockets.messaging.handlers.RequestHandler;
import com.starfishst.core.utils.sockets.messaging.handlers.ResponseGiver;
import com.starfishst.core.utils.sockets.messaging.type.response.VoidResponse;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

/** Handles that the client was disconnected from the server */
public class DisconnectedHandler implements RequestHandler {

  /** The client that is handling that has been disconnected */
  @NotNull private final Client client;

  /**
   * Create the handler
   *
   * @param client the client waiting to be disconnected
   */
  public DisconnectedHandler(@NotNull Client client) {
    this.client = client;
  }

  @Override
  public @NotNull String method() {
    return "disconnected";
  }

  @Override
  public @NotNull SocketResponse getResponse(
      @NotNull SocketRequest request, @NotNull IMessenger messenger) {
    try {
      client.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ResponseGiver.removeHandler(this);
    return new VoidResponse();
  }
}

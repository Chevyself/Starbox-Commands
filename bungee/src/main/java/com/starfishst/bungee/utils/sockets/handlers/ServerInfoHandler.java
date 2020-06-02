package com.starfishst.bungee.utils.sockets.handlers;

import com.starfishst.bungee.utils.sockets.BungeeSocketServer;
import com.starfishst.core.utils.sockets.messaging.IMessenger;
import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import com.starfishst.core.utils.sockets.messaging.SocketResponse;
import com.starfishst.core.utils.sockets.messaging.handlers.RequestHandler;
import com.starfishst.core.utils.sockets.messaging.type.response.VoidResponse;
import com.starfishst.core.utils.sockets.server.ClientThread;
import org.jetbrains.annotations.NotNull;

/**
 * Provides the bungee socket with important information such as the port that is used to
 * differentiate from other servers
 */
public class ServerInfoHandler implements RequestHandler {

  /** The server waiting for server info */
  @NotNull private final BungeeSocketServer server;

  /**
   * Create the handler
   *
   * @param server that needs server information
   */
  public ServerInfoHandler(@NotNull BungeeSocketServer server) {
    this.server = server;
  }

  /**
   * The method that this handler manages
   *
   * @return the method as string
   */
  @Override
  public @NotNull String method() {
    return "server-info";
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
    if (messenger instanceof ClientThread) {
      server.setName((ClientThread) messenger, request.getData().get("name"));
    }
    return new VoidResponse();
  }
}

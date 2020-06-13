package com.starfishst.bukkit.utils.sockets.requests;

import com.starfishst.core.utils.maps.Maps;
import com.starfishst.core.utils.sockets.messaging.type.request.VoidRequest;
import org.jetbrains.annotations.NotNull;

/** Sends the information about this server to the bungee when connecting */
public class ServerInfoRequest extends VoidRequest {

  /**
   * Create a request to send
   *
   * @param serverName the name of the server
   */
  public ServerInfoRequest(@NotNull String serverName) {
    super(Maps.singleton("name", serverName), "server-info");
  }
}

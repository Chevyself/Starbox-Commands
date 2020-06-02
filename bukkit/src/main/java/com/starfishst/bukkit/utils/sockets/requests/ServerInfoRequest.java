package com.starfishst.bukkit.utils.sockets.requests;

import com.starfishst.core.utils.Maps;
import com.starfishst.core.utils.sockets.messaging.type.request.VoidRequest;
import org.bukkit.Bukkit;

/** Sends the information about this server to the bungee when connecting */
public class ServerInfoRequest extends VoidRequest {

  /** Create a request to send */
  public ServerInfoRequest() {
    super(Maps.singleton("name", Bukkit.getName()), "server-info");
  }
}

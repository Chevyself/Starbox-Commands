package com.starfishst.bungee.utils.sockets.request;

import com.starfishst.core.utils.sockets.messaging.type.request.VoidRequest;
import java.util.HashMap;

/** The request to send to bukkit servers when the bungee server is disconnected */
public class BungeeDisconnectedRequest extends VoidRequest {

  /** Create a request to send */
  public BungeeDisconnectedRequest() {
    super(new HashMap<>(), "bungee-disconnected");
  }
}

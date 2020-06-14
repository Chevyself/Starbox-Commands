package com.starfishst.core.utils.sockets.messaging.requests;

import com.starfishst.core.utils.maps.Maps;
import com.starfishst.core.utils.sockets.messaging.SocketRequest;

/** A request for the ping between sockets */
public class PingRequest extends SocketRequest {

  /** Create the request to send */
  public PingRequest() {
    super(Maps.singleton("init", String.valueOf(System.currentTimeMillis())), "ping", false);
  }
}

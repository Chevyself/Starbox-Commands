package com.starfishst.core.utils.sockets.messaging.type.request;

import java.util.HashMap;

/** The client requests to be disconnected from the socket server */
public class DisconnectRequest extends VoidRequest {

  /** Create a request to send */
  public DisconnectRequest() {
    super(new HashMap<>(), "disconnect");
  }
}

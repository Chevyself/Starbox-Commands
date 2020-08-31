package com.starfishst.core.utils.sockets.messaging.requests;

import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import java.util.HashMap;

/** Sends a heartbeat to the other socket */
public class HeartbeatRequest extends SocketRequest {

  /** Create a request to send */
  public HeartbeatRequest() {
    super(new HashMap<>(), "heartbeat", false);
  }
}

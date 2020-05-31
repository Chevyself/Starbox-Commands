package com.starfishst.core.utils.sockets.messaging.type.response;

import com.starfishst.core.utils.sockets.messaging.SocketResponse;
import java.util.HashMap;

/**
 * A response to a void request
 */
public class VoidResponse extends SocketResponse {

  /**
   * Create the response with no data
   */
  public VoidResponse() {
    super(new HashMap<>());
  }
}

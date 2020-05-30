package com.starfishst.core.utils.sockets.messaging.type.response;

import com.starfishst.core.utils.sockets.messaging.SocketResponse;
import java.util.HashMap;

public class VoidResponse extends SocketResponse {

  public VoidResponse() {
    super(new HashMap<>());
  }
}

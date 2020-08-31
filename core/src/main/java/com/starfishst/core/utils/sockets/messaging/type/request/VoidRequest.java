package com.starfishst.core.utils.sockets.messaging.type.request;

import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/** A void socket request */
public class VoidRequest extends SocketRequest {

  /**
   * Create a request to send
   *
   * @param data the data to include in the request
   * @param method the method used in the request
   */
  public VoidRequest(@NotNull HashMap<String, String> data, @NotNull String method) {
    super(data, method, true);
  }
}

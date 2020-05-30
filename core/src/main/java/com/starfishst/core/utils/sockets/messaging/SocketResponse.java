package com.starfishst.core.utils.sockets.messaging;

import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/** A message that is send as a response of a request from another socket */
public class SocketResponse extends SocketMessage {

  /**
   * Create a socket response
   *
   * @param data to send in the response message
   */
  public SocketResponse(@NotNull HashMap<String, String> data) {
    super(data);
  }

  @Override
  public @NotNull SocketMessageType getType() {
    return SocketMessageType.RESPONSE;
  }
}

package com.starfishst.core.utils.sockets.messaging;

import com.starfishst.core.utils.maps.MapBuilder;
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

  /**
   * Create a socket response
   *
   * @param data to sen in the response message
   */
  public SocketResponse(@NotNull MapBuilder<String, String> data) {
    super(data.build());
  }

  @Override
  public @NotNull SocketMessageType getType() {
    return SocketMessageType.RESPONSE;
  }

  @Override
  public String toString() {
    return "SocketResponse{" + "data=" + data + '}';
  }
}

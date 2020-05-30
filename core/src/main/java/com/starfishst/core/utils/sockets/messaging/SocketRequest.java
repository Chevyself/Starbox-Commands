package com.starfishst.core.utils.sockets.messaging;

import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/** A socket message that expects a response with an object from the other socket */
public class SocketRequest extends SocketMessage {

  /** Check if a request is void */
  private final boolean isVoid;

  /**
   * Create a request to send
   *
   * @param data the data to include in the request
   * @param method the method used in the request
   * @param isVoid if the request is void and is not waiting for a response
   */
  public SocketRequest(
      @NotNull HashMap<String, String> data, @NotNull String method, boolean isVoid) {
    super(data);
    this.isVoid = isVoid;
    data.put("method", method);
    data.put("void", String.valueOf(isVoid));
  }

  /**
   * If the request is void
   *
   * @return if the request is void
   */
  public boolean isVoid() {
    return isVoid;
  }

  @Override
  public @NotNull SocketMessageType getType() {
    return SocketMessageType.REQUEST;
  }
}

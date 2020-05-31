package com.starfishst.core.utils.sockets.messaging.type.request;

import com.starfishst.core.utils.Maps;
import org.jetbrains.annotations.NotNull;

/** The requests send by the server to the client telling it that it has been disconnected */
public class DisconnectedRequest extends VoidRequest {

  /**
   * Create a disconnected request
   *
   * @param reason the reason why the client was disconnected
   */
  public DisconnectedRequest(@NotNull String reason) {
    super(Maps.singleton("reason", reason), "disconnected");
  }
}

package com.starfishst.core.utils.sockets.messaging;

import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/**
 * The types of messages that sockets can send. Used to differentiate when a socket is reading it
 */
public enum SocketMessageType {
  /** A simple message can also be called a void request */
  MESSAGE,
  /** A message that is requesting a response from the other socket */
  REQUEST,
  /** A message that is send when a request is handled */
  RESPONSE;

  /**
   * Get the message type from the data provided by a message
   *
   * @param data the data
   * @return the message type
   */
  @NotNull
  public static SocketMessageType fromData(@NotNull HashMap<String, String> data) {
    return valueOf(data.getOrDefault("type", "MESSAGE"));
  }
}

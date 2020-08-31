package com.starfishst.core.utils.sockets.messaging;

import com.starfishst.core.utils.maps.MapBuilder;
import com.starfishst.core.utils.maps.Maps;
import com.starfishst.core.utils.sockets.exception.IllegalRequestTypeException;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/**
 * A socket message is send between sockets. It contains data, which can be requested or just send
 */
public class SocketMessage {

  /** The data of the message */
  @NotNull protected final HashMap<String, String> data;

  /**
   * Create the message
   *
   * @param data the data to send in the message
   */
  public SocketMessage(@NotNull HashMap<String, String> data) {
    this.data = data;
    checkType();
  }

  /**
   * Create the message
   *
   * @param data the data to send in the message
   */
  public SocketMessage(@NotNull MapBuilder<String, String> data) {
    this(data.build());
  }

  /**
   * Checks if the data in the message contains the type, if it does not have a type this method
   * adds it automatically
   */
  private void checkType() {
    if (!this.data.containsKey("type")) {
      this.data.put("type", getType().toString());
    }
    try {
      SocketMessageType.fromData(this.data);
    } catch (IllegalArgumentException e) {
      throw new IllegalRequestTypeException(this + " has an illegal type!", e);
    }
  }

  /**
   * Puts all the data in a single string that can be send
   *
   * @return the built data
   */
  @NotNull
  public String build() {
    return Maps.toString(getData());
  }

  /**
   * Get the data that this socket has
   *
   * @return the data
   */
  @NotNull
  public HashMap<String, String> getData() {
    return data;
  }

  /**
   * Get the type of the message
   *
   * @return the type
   */
  @NotNull
  public SocketMessageType getType() {
    return SocketMessageType.MESSAGE;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof SocketMessage)) return false;

    SocketMessage that = (SocketMessage) object;

    return data.equals(that.data);
  }

  @Override
  public int hashCode() {
    return data.hashCode();
  }

  @Override
  public String toString() {
    return "SocketMessage{" + "data=" + data + '}';
  }
}

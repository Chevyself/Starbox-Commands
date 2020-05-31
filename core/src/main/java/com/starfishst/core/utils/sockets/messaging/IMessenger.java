package com.starfishst.core.utils.sockets.messaging;

import com.starfishst.core.fallback.Fallback;
import com.starfishst.core.utils.sockets.exception.SocketException;
import com.starfishst.core.utils.sockets.messaging.handlers.ResponseGiver;
import com.starfishst.core.utils.sockets.messaging.type.response.ExceptionResponse;
import java.io.IOException;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/** The simplest form of messenger */
public interface IMessenger {

  /**
   * Process the received data
   *
   * @param data the data received
   */
  default void processData(@NotNull HashMap<String, String> data) {
    SocketMessageType type = SocketMessageType.fromData(data);
    switch (type) {
      case REQUEST:
        try {
          sendResponse(
              ResponseGiver.getResponse(
                  new SocketRequest(
                      data,
                      data.getOrDefault("method", "empty"),
                      Boolean.parseBoolean(data.getOrDefault("void", "true"))),
                  this));
        } catch (SocketException e) {
          sendResponse(new ExceptionResponse(e.getMessage()));
        }
        break;
      case MESSAGE:
        // Handled as a void request
        try {
          ResponseGiver.getResponse(
              new SocketRequest(data, data.getOrDefault("method", "empty"), true), this);
        } catch (SocketException e) {
          Fallback.addError(e.getMessage());
          e.printStackTrace();
        }
    }
  }

  /**
   * Send the response requested by another socket
   *
   * @param response to send
   */
  void sendResponse(@NotNull SocketResponse response);

  /**
   * Send a request and wait for a response from another socket
   *
   * @param request the request waiting for a response
   * @return the response
   * @throws IOException in case something goes wrong
   */
  @NotNull
  SocketResponse sendRequest(@NotNull SocketRequest request) throws IOException;
}

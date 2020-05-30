package com.starfishst.core.utils.sockets.messaging;

import com.starfishst.core.utils.Maps;
import com.starfishst.core.utils.sockets.exception.SocketException;
import com.starfishst.core.utils.sockets.messaging.handlers.ResponseGiver;
import com.starfishst.core.utils.sockets.messaging.type.response.ExceptionResponse;
import com.starfishst.core.utils.sockets.messaging.type.response.VoidResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/** A socket object that can send messages */
public interface IMessenger {

  /**
   * Send a request and wait for a response from another socket
   *
   * @param request the request waiting for a response
   * @return the response
   * @throws IOException in case something goes wrong
   */
  @NotNull
  default SocketResponse sendRequest(@NotNull SocketRequest request) throws IOException {
    getChannelOut().println(request.build());
    if (!request.isVoid()) {
      String line = this.getChannelIn().readLine();
      if (line != null) {
        return new SocketResponse(Maps.fromString(line));
      } else {
        throw new IllegalArgumentException("The other socket did not send anything");
      }
    }
    return new VoidResponse();
  }

  /**
   * Send the response requested by another socket
   *
   * @param response to send
   */
  default void sendResponse(@NotNull SocketResponse response) {
    getChannelOut().println(response.build());
  }

  /**
   * TODO
   * @throws IOException
   */
  default void listen() throws IOException {
    BufferedReader in = getChannelIn();
    if (in.ready()) {
      String line = in.readLine();
      HashMap<String, String> data = Maps.fromString(line);
      SocketMessageType type = SocketMessageType.fromData(data);
      switch (type) {
        case REQUEST:
          try {
            sendResponse(
                ResponseGiver.getResponse(
                    new SocketRequest(
                        data,
                        data.getOrDefault("method", "empty"),
                        Boolean.parseBoolean(data.getOrDefault("void", "true"))), this));
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
            e.printStackTrace();
          }
      }
    }
  }

  /**
   * Get the channel that reads information received
   *
   * @return the channel
   */
  @NotNull
  BufferedReader getChannelIn();

  /**
   * Get the channel that is used to send information out
   *
   * @return the channel
   */
  @NotNull
  PrintWriter getChannelOut();
}

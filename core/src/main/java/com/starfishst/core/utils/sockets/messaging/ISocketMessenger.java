package com.starfishst.core.utils.sockets.messaging;

import com.starfishst.core.utils.maps.Maps;
import com.starfishst.core.utils.sockets.messaging.type.response.VoidResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/** A socket object that can send messages */
public interface ISocketMessenger extends IMessenger {

  /**
   * Listen for the messages received by this socket and handle them
   *
   * @throws IOException if the buffered reader fails to read the line in
   */
  default void listen() throws IOException {
    BufferedReader in = getChannelIn();
    if (in.ready()) {
      HashMap<String, String> data = Maps.fromString(in.readLine());
      this.processData(data);
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
        throw new IllegalStateException("The other socket did not send anything");
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
}

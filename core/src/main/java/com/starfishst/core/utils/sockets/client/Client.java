package com.starfishst.core.utils.sockets.client;

import com.starfishst.core.utils.sockets.messaging.IMessenger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.jetbrains.annotations.NotNull;

/** A client that can connect to a {@link com.starfishst.core.utils.sockets.server.Server} */
public class Client implements IMessenger {

  /** The actual socket client */
  @NotNull protected final Socket client;
  /** The channel out */
  @NotNull private final PrintWriter out;
  /** The channel in */
  @NotNull private final BufferedReader in;

  /**
   * Connect to a socket server
   *
   * @param ip of the socket
   * @param port of the socket
   * @throws IOException if the connection goes wrong
   */
  public Client(@NotNull String ip, int port) throws IOException {
    this.client = new Socket(ip, port);
    this.out = new PrintWriter(this.client.getOutputStream(), true);
    this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
  }

  /**
   * Closes the client
   *
   * @throws IOException when closing could not be performed
   */
  public void close() throws IOException {
    this.in.close();
    this.out.close();
    this.client.close();
  }

  @Override
  public @NotNull BufferedReader getChannelIn() {
    return in;
  }

  @Override
  public @NotNull PrintWriter getChannelOut() {
    return out;
  }
}

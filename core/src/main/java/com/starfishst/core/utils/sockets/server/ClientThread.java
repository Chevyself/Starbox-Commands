package com.starfishst.core.utils.sockets.server;

import com.starfishst.core.utils.cache.Catchable;
import com.starfishst.core.utils.sockets.messaging.ISocketMessenger;
import com.starfishst.core.utils.sockets.messaging.type.request.DisconnectedRequest;
import com.starfishst.core.utils.time.Time;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.jetbrains.annotations.NotNull;

/**
 * This is the client that is connected to a server socket.
 *
 * <p>It is a catchable because if you keep clients in a socket for too long it will consume CPU
 * resources.
 */
public class ClientThread extends Catchable implements ISocketMessenger {

  /** The server that this client is connected to */
  @NotNull protected final Server server;
  /** The actual socket client that is connected to the server */
  @NotNull protected final Socket client;
  /** The channel out */
  @NotNull private final PrintWriter out;
  /** The channel in */
  @NotNull private final BufferedReader in;

  /**
   * Create an instance
   *
   * @param toRemove the time to remove the object from the cache
   * @param server the server that the client connected to
   * @param client the client that connected to the server
   * @throws IOException if the client connection goes wrong
   */
  public ClientThread(@NotNull Time toRemove, @NotNull Server server, @NotNull Socket client)
      throws IOException {
    super(toRemove);
    this.server = server;
    this.client = client;
    this.out = new PrintWriter(client.getOutputStream(), true);
    this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
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

  /**
   * Get the actual socket client
   *
   * @return the socket client
   */
  @NotNull
  public Socket getClient() {
    return client;
  }

  @Override
  public void onSecondsPassed() {}

  @Override
  public void onRemove() {
    try {
      sendRequest(new DisconnectedRequest("Time exceeded"));
      close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

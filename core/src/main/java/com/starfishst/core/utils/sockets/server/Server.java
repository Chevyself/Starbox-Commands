package com.starfishst.core.utils.sockets.server;

import com.starfishst.core.fallback.Fallback;
import com.starfishst.core.utils.time.Time;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/** This is a socket server. It has to be registered inside a thread */
public abstract class Server implements Runnable {

  /** The actual server socket */
  @NotNull protected final ServerSocket server;
  /** The list of clients connected to this socket */
  @NotNull protected final List<ClientThread> threads;
  /** The time to disconnect clients */
  @NotNull private final Time toRemove;

  /**
   * Start a server
   *
   * @param port the socket that the socket should listen to
   * @param toRemove the time for the clients to disconnect
   * @throws IOException in case that server cannot be created
   */
  public Server(int port, @NotNull Time toRemove) throws IOException {
    this.server = new ServerSocket(port);
    this.threads = new ArrayList<>();
    this.toRemove = toRemove;
  }

  /**
   * Registers the client in a task to listen
   *
   * @param client the client to register in the task
   */
  public abstract void startClientTask(@NotNull ClientThread client);

  /**
   * Accepts a client socket
   *
   * @param socket the client to accept
   * @throws IOException in case that the connection goes wrong
   */
  public void addClient(@NotNull Socket socket) throws IOException {
    ClientThread client = new ClientThread(toRemove, this, socket);
    threads.add(client);
    startClientTask(client);
  }

  /**
   * Disconnects a client from the server
   *
   * @param client to disconnect
   */
  public void disconnectClient(@NotNull ClientThread client) {
    threads.remove(client);
  }

  /**
   * Closes the server
   *
   * @throws IOException if it closed with errors or wasn't open
   */
  public void close() throws IOException {
    this.server.close();
  }

  /**
   * Get the clients connected to the server
   *
   * @return a list of clients connected to the server
   */
  @NotNull
  public List<ClientThread> getThreads() {
    return threads;
  }

  /**
   * Get the actual socket server
   *
   * @return the socket server
   */
  @NotNull
  public ServerSocket getServer() {
    return server;
  }

  @Override
  public void run() {
    try {
      this.addClient(this.server.accept());
    } catch (IOException e) {
      Fallback.addError("Socket could not be added");
      e.printStackTrace();
    }
  }
}

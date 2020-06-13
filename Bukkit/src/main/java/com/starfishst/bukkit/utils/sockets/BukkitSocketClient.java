package com.starfishst.bukkit.utils.sockets;

import com.starfishst.bukkit.utils.sockets.requests.ServerInfoRequest;
import com.starfishst.core.utils.sockets.client.Client;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

/** Creates a Bukkit socket client */
public class BukkitSocketClient extends Client {

  /** The task where the client is running */
  @NotNull private final BukkitTask task;
  /** The name of the server where this client was created */
  @NotNull private final String serverName;

  /**
   * Connect to a socket server
   *
   * @param ip of the socket
   * @param port of the socket
   * @param plugin the plugin owner of the socket
   * @param serverName the name of the server
   * @throws IOException if the connection goes wrong
   */
  public BukkitSocketClient(
      @NotNull String ip, int port, @NotNull Plugin plugin, @NotNull String serverName)
      throws IOException {
    super(ip, port);
    this.task =
        Bukkit.getScheduler()
            .runTaskTimerAsynchronously(
                plugin,
                () -> {
                  try {
                    this.listen();
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                },
                0L,
                1L);
    this.serverName = serverName;
    sendRequest(new ServerInfoRequest(serverName));
  }

  /**
   * Get the bukkit task where the client is running
   *
   * @return the bukkit task
   */
  @NotNull
  public BukkitTask getTask() {
    return task;
  }

  /**
   * Get the name of the server where this client was created
   *
   * @return the name of the server
   */
  @NotNull
  public String getServerName() {
    return serverName;
  }

  @Override
  public void close() throws IOException {
    task.cancel();
    super.close();
  }
}

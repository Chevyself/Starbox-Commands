package com.starfishst.bungee.utils.sockets;

import com.starfishst.core.fallback.Fallback;
import com.starfishst.core.utils.NullableAtomic;
import com.starfishst.core.utils.sockets.server.ClientThread;
import com.starfishst.core.utils.sockets.server.Server;
import com.starfishst.core.utils.time.Time;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A socket server for bungee */
public class BungeeSocketServer extends Server {

  /** The plugin where the socket server was created */
  @NotNull private final Plugin plugin;
  /** The task where the socket is listening */
  @NotNull private final ScheduledTask task;
  /** The clients connected to the server */
  @NotNull private final HashMap<ClientThread, ScheduledTask> clients = new HashMap<>();
  /** The HashMap with the ports of each client thread */
  @NotNull private final HashMap<ClientThread, String> names = new HashMap<>();

  /**
   * Start a server
   *
   * @param port the socket that the socket should listen to
   * @param toRemove the time for the clients to disconnect
   * @param plugin where the socket will be running
   * @throws IOException in case that server cannot be created
   */
  public BungeeSocketServer(int port, @NotNull Time toRemove, @NotNull Plugin plugin)
      throws IOException {
    super(port, toRemove);
    this.plugin = plugin;
    this.task =
        plugin.getProxy().getScheduler().schedule(plugin, this, 0, 1, TimeUnit.MILLISECONDS);
  }

  /**
   * Add the server name of a client
   *
   * @param client the client
   * @param name the name
   */
  public void setName(@NotNull ClientThread client, @NotNull String name) {
    names.put(client, name);
  }

  /**
   * Get the server name of a client
   *
   * @param client the client to get the port from
   * @return the port
   */
  @NotNull
  public String getName(@NotNull ClientThread client) {
    return names.getOrDefault(client, "");
  }

  /**
   * Get the server where the thread is coming from
   *
   * @param thread the client to get the server where is coming from
   * @return the server
   */
  @NotNull
  public ServerInfo getServer(@NotNull ClientThread thread) {
    return ProxyServer.getInstance().getServerInfo(getName(thread));
  }

  /**
   * Get the client of a server using its name
   *
   * @param name the name of the server
   * @return the client if found else null
   */
  @Nullable
  public ClientThread getClient(@NotNull String name) {
    NullableAtomic<ClientThread> atomic = new NullableAtomic<>();
    names.forEach(
        (client, serverName) -> {
          if (serverName.equalsIgnoreCase(name)) {
            atomic.set(client);
          }
        });
    return atomic.get();
  }

  /**
   * Get the clients and the name of the servers where the client is connected
   *
   * @return the clients
   */
  @NotNull
  public HashMap<ClientThread, String> getClients() {
    return names;
  }

  @Override
  public void startClientTask(@NotNull ClientThread client) {
    clients.put(
        client,
        plugin
            .getProxy()
            .getScheduler()
            .schedule(
                plugin,
                () -> {
                  try {
                    client.listen();
                  } catch (IOException e) {
                    Fallback.addError(e.getMessage());
                    e.printStackTrace();
                  }
                },
                0,
                1,
                TimeUnit.MILLISECONDS));
  }

  /**
   * Disconnects a client from the server
   *
   * @param client to disconnect
   */
  @Override
  public void disconnectClient(@NotNull ClientThread client) {
    ScheduledTask task = clients.get(client);
    if (task != null) {
      task.cancel();
    }
    clients.remove(client);
    names.remove(client);
    super.disconnectClient(client);
  }
}

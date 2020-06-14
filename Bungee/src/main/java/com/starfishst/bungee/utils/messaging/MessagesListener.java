package com.starfishst.bungee.utils.messaging;

import com.starfishst.core.utils.maps.Maps;
import com.starfishst.core.utils.sockets.messaging.IMessenger;
import com.starfishst.core.utils.sockets.messaging.SocketMessageType;
import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import com.starfishst.core.utils.sockets.messaging.SocketResponse;
import com.starfishst.core.utils.sockets.messaging.type.response.VoidResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

/** A listener for plugin messages that are treated as socket messages */
public class MessagesListener implements Listener {

  /** The time to timeout a request */
  private static int timeout = 10000;

  /** The name of the channel that this will listen to */
  @NotNull private final String channel;
  /** The plugin where this listener is working */
  @NotNull private final Plugin plugin;
  /** Contains the response that the server was waiting for */
  @NotNull private final HashMap<SenderServer, SocketResponse> responses = new HashMap<>();
  /** Contains the callback that the server was waiting for */
  @NotNull
  private final HashMap<SenderServer, Consumer<SocketResponse>> callbacks = new HashMap<>();
  /**
   * Start the listener
   *
   * @param channel that the listener should listen to
   * @param plugin the plugin that is using this listener
   */
  public MessagesListener(@NotNull String channel, @NotNull Plugin plugin) {
    this.channel = channel;
    this.plugin = plugin;
    plugin.getProxy().registerChannel(channel);
    plugin.getProxy().getPluginManager().registerListener(plugin, this);
  }

  /**
   * Listens to the plugin messages
   *
   * @param event of a message received
   */
  @EventHandler
  public void onPluginMessage(PluginMessageEvent event) {
    if (event.getTag().equalsIgnoreCase(channel)) {
      DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
      try {
        HashMap<String, String> data = Maps.fromString(in.readUTF());
        SenderServer senderServer =
            new SenderServer(
                plugin.getProxy().getPlayer(event.getReceiver().toString()).getServer().getInfo());
        senderServer.processData(data);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Sends a request to the desired server
   *
   * @param server the server to send the request
   * @param request the request
   * @return the response to the request
   * @throws NullPointerException if this invoked from a synchronous method because it wont be able
   *     to be able get the response
   */
  @NotNull
  public SocketResponse sendRequest(@NotNull ServerInfo server, @NotNull SocketRequest request) {
    return new SenderServer(server).sendRequest(request);
  }

  /** Stops the messages listener */
  public void stop() {
    plugin.getProxy().unregisterChannel(channel);
  }

  /**
   * Set the time in which request times out The default time is 10000ms
   *
   * @param timeout the time to timeout in millis
   */
  public static void setTimeout(int timeout) {
    MessagesListener.timeout = timeout;
  }

  /**
   * Get the time to timeout the request
   *
   * @return the time in millis
   */
  public static int getTimeout() {
    return timeout;
  }

  /** A temporal server that is sending data */
  class SenderServer implements IMessenger {

    /** The actual bungee server */
    @NotNull private final ServerInfo server;

    /**
     * Create a server that is sending and receiving data
     *
     * @param server the server
     */
    SenderServer(@NotNull ServerInfo server) {
      this.server = server;
    }

    /**
     * Sends the data to the bukkit server
     *
     * @param data to send
     */
    private void sendData(@NotNull String data) {
      try {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        out.writeUTF(data);
        server.sendData(channel, stream.toByteArray());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public void sendResponse(@NotNull SocketResponse response) {
      this.sendData(response.build());
    }

    @Override
    public void processData(@NotNull HashMap<String, String> data) {
      SocketMessageType type = SocketMessageType.fromData(data);
      if (type == SocketMessageType.RESPONSE) {
        responses.put(this, new SocketResponse(data));
      } else {
        IMessenger.super.processData(data);
      }
    }

    @Override
    public @NotNull SocketResponse sendRequest(@NotNull SocketRequest request) {
      this.sendData(request.build());
      if (!request.isVoid()) {
        int millis = 0;
        while (!responses.containsKey(this)) {
          if (millis < timeout) {
            try {
              Thread.sleep(1);
              millis++;
            } catch (InterruptedException e) {
              e.printStackTrace();
              break;
            }
          } else {
            throw new IllegalStateException(request + " timed out... Is it running synchronously?");
          }
        }
        SocketResponse response = responses.get(this);
        responses.remove(this);
        return response;
      }
      return new VoidResponse();
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) return true;
      if (!(object instanceof SenderServer)) return false;

      SenderServer that = (SenderServer) object;

      return server.equals(that.server);
    }

    @Override
    public int hashCode() {
      return server.hashCode();
    }
  }
}

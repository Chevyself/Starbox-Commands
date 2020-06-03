package com.starfishst.bungee.utils.messaging;

import com.starfishst.core.utils.Maps;
import com.starfishst.core.utils.sockets.messaging.IMessenger;
import com.starfishst.core.utils.sockets.messaging.SocketMessageType;
import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import com.starfishst.core.utils.sockets.messaging.SocketResponse;
import com.starfishst.core.utils.sockets.messaging.type.response.VoidResponse;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

/** A listener for plugin messages that are treated as socket messages */
public class MessagesListener implements Listener {

  /** The name of the channel that this will listen to */
  @NotNull private final String channel;
  /** The plugin where this listener is working */
  @NotNull private final Plugin plugin;
  /** Contains the response that the server was waiting for */
  @NotNull private final HashMap<SenderServer, SocketResponse> responses = new HashMap<>();

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
   */
  @NotNull
  public SocketResponse sendRequest(@NotNull ServerInfo server, @NotNull SocketRequest request) {
    return new SenderServer(server).sendRequest(request);
  }

  public void stop() {
    plugin.getProxy().unregisterChannel(channel);
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
        notifyAll();
      } else {
        IMessenger.super.processData(data);
      }
    }

    @Override
    public @NotNull SocketResponse sendRequest(@NotNull SocketRequest request) {
      this.sendData(request.build());
      if (!request.isVoid()) {
        while (responses.get(this) == null) {
          try {
            wait(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
            break;
          }
        }
        if (responses.containsKey(this)) {
          return responses.get(this);
        } else {
          throw new IllegalStateException("The other socket did not send anything");
        }
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

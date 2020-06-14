package com.starfishst.bukkit.utils.messaging;

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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A plugin messages listener for messages that are treated as socket messages */
public class MessagesListener implements PluginMessageListener {

  /** The time to timeout a request */
  private static int timeout = 10000;

  /** The channel where this listener will be working */
  @NotNull private final String channel;
  /** The plugin where this listener was registered */
  @NotNull private final Plugin plugin;
  /** The list of responses that this listener has */
  @NotNull private final HashMap<PlayerSender, SocketResponse> responses = new HashMap<>();

  /**
   * Create an instance
   *
   * @param channel the channel that this listener should use
   * @param plugin the plugin that should register the channel
   */
  public MessagesListener(@NotNull String channel, @NotNull Plugin plugin) {
    this.channel = channel;
    this.plugin = plugin;
    plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel);
    plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, channel, this);
  }

  /**
   * Sends a request
   *
   * @param request to send
   * @return the response or null if there was no available player to send it {@link #available()}
   * @throws IOException if the request went wrong
   * @throws NullPointerException if this invoked from a synchronous method because it wont be able
   *     to be able get the response
   */
  @Nullable
  public SocketResponse sendRequest(@NotNull SocketRequest request) throws IOException {
    PlayerSender player = getRandomPlayer();
    if (player != null) {
      return player.sendRequest(request);
    } else {
      return null;
    }
  }

  /**
   * Check if there's players available to send requests
   *
   * @return if a player is available to send requests
   */
  public boolean available() {
    return getRandomPlayer() != null;
  }

  /**
   * Gets a player that can be used to send a request
   *
   * @return the player that can be used to send the request
   * @throws IllegalStateException if there's no players online
   */
  @Nullable
  public PlayerSender getRandomPlayer() {
    if (Bukkit.getOnlinePlayers().isEmpty()) {
      throw new IllegalStateException("Cannot send plugin messages when there's no online players");
    } else {
      for (Player player : Bukkit.getOnlinePlayers()) {
        PlayerSender sender = new PlayerSender(player);
        if (!responses.containsKey(sender)) {
          return sender;
        }
      }
      return null;
    }
  }

  @Override
  public void onPluginMessageReceived(
      @NotNull String channel, @NotNull Player player, @NotNull byte[] bytes) {
    DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
    try {
      HashMap<String, String> data = Maps.fromString(in.readUTF());
      PlayerSender senderServer = new PlayerSender(player);
      senderServer.processData(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
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

  /** The player that is sending data */
  class PlayerSender implements IMessenger {

    /** The actual player */
    @NotNull private final Player player;

    /**
     * Create an instance
     *
     * @param player the player
     */
    PlayerSender(@NotNull Player player) {
      this.player = player;
    }

    /**
     * Send the data to the bungee server
     *
     * @param data the data to send
     * @throws IOException in case that it cannot be converted to bytes
     */
    private void sendData(@NotNull String data) throws IOException {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(stream);
      out.writeUTF(data);
      player.sendPluginMessage(plugin, channel, stream.toByteArray());
    }

    @Override
    public void sendResponse(@NotNull SocketResponse response) {
      try {
        sendData(response.build());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public @NotNull SocketResponse sendRequest(@NotNull SocketRequest request) throws IOException {
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
    public void processData(@NotNull HashMap<String, String> data) {
      SocketMessageType type = SocketMessageType.fromData(data);
      if (type == SocketMessageType.RESPONSE) {
        responses.put(this, new SocketResponse(data));
      } else {
        IMessenger.super.processData(data);
      }
    }
  }
}

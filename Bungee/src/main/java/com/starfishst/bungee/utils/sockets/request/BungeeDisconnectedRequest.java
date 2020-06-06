package com.starfishst.bungee.utils.sockets.request;

import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import com.starfishst.core.utils.sockets.messaging.type.request.VoidRequest;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * The request to send to bukkit servers when the bungee server is disconnected
 */
public class BungeeDisconnectedRequest extends VoidRequest {

    /**
     * Create a request to send
     *
     * @param data   the data to include in the request
     * @param method the method used in the request
     */
    public BungeeDisconnectedRequest(@NotNull HashMap<String, String> data, @NotNull String method) {
        super(new HashMap<>(), "bungee-disconnected");
    }

}

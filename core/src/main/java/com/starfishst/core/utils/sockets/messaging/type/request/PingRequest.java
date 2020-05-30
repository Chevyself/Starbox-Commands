package com.starfishst.core.utils.sockets.messaging.type.request;

import com.starfishst.core.utils.Maps;
import com.starfishst.core.utils.sockets.messaging.SocketRequest;

public class PingRequest extends SocketRequest {

    /**
     * Create a ping request to send
     *
     */
    public PingRequest() {
        super(Maps.singleton("init", String.valueOf(System.currentTimeMillis())), "ping", false);
    }

}

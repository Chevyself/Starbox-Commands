package com.starfishst.core.utils.sockets.messaging.handlers.type;

import com.starfishst.core.utils.sockets.messaging.IMessenger;
import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import com.starfishst.core.utils.sockets.messaging.SocketResponse;
import com.starfishst.core.utils.sockets.messaging.handlers.RequestHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Handles ping requests
 */
public class PingHandler implements RequestHandler {

    @Override
    public @NotNull String method() {
        return "ping";
    }

    @Override
    public @NotNull SocketResponse getResponse(@NotNull SocketRequest request, @NotNull IMessenger messenger) {
        HashMap<String, String> data = new HashMap<>();
        data.put("init", request.getData().getOrDefault("init", "0"));
        data.put("end", String.valueOf(System.currentTimeMillis()));
        return new SocketResponse(data);
    }

}

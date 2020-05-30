package com.starfishst.core.utils.sockets.messaging.handlers.type;

import com.starfishst.core.utils.sockets.messaging.IMessenger;
import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import com.starfishst.core.utils.sockets.messaging.SocketResponse;
import com.starfishst.core.utils.sockets.messaging.handlers.RequestHandler;
import com.starfishst.core.utils.sockets.messaging.type.response.VoidResponse;
import com.starfishst.core.utils.sockets.server.ClientThread;
import com.starfishst.core.utils.sockets.server.Server;
import org.jetbrains.annotations.NotNull;

/**
 * Handles the request of a client to be disconnected
 */
public class DisconnectHandler implements RequestHandler {

    /**
     * The server handling the disconnect requests
     */
    @NotNull
    private final Server server;

    /**
     * Create the handler
     *
     * @param server the server that has to disconnect the client
     */
    public DisconnectHandler(@NotNull Server server) {
        this.server = server;
    }

    @Override
    public @NotNull String method() {
        return "disconnect";
    }

    @Override
    public @NotNull SocketResponse getResponse(@NotNull SocketRequest request, @NotNull IMessenger messenger) {
        if (messenger instanceof ClientThread) {
            server.disconnectClient((ClientThread) messenger);
        }
        return new VoidResponse();
    }

}

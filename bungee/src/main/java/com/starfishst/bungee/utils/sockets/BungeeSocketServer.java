package com.starfishst.bungee.utils.sockets;

import com.starfishst.core.utils.sockets.server.ClientThread;
import com.starfishst.core.utils.sockets.server.Server;
import com.starfishst.core.utils.time.Time;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

public class BungeeSocketServer extends Server {

    /**
     * The plugin where the socket server was created
     */
    @NotNull
    private final Plugin plugin;
    /**
     * The task where the socket is listening
     */
    @NotNull
    private final ScheduledTask task;
    /**
     * The clients connected to the server
     */
    @NotNull
    private final HashMap<ClientThread, ScheduledTask> clients = new HashMap<>();

    /**
     * Start a server
     *
     * @param port     the socket that the socket should listen to
     * @param toRemove the time for the clients to disconnect
     * @param plugin where the socket will be running
     * @throws IOException in case that server cannot be created
     */
    public BungeeSocketServer(int port, @NotNull Time toRemove, @NotNull Plugin plugin) throws IOException {
        super(port, toRemove);
        this.plugin = plugin;
        this.task = plugin.getProxy().getScheduler().runAsync(plugin, new ServerTask(this));
    }

    @Override
    public void startClientTask(@NotNull ClientThread client) {
        clients.put(client, plugin.getProxy().getScheduler().runAsync(plugin, new ClientThreadTask(client)));
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
            clients.remove(client);
            if (task instanceof ClientThreadTask) {
                ((ClientThreadTask) task).cancelTask();
            }
            task.cancel();
        }
        super.disconnectClient(client);

    }

    /**
     * The task for client threads
     */
    static class ClientThreadTask implements Runnable {

        /**
         * The client owner of the thread
         */
        @NotNull
        private final ClientThread client;
        /**
         * Whether the task is running
         */
        private boolean running = true;

        /**
         * Create the task
         *
         * @param client the client owner of the task
         */
        ClientThreadTask(@NotNull ClientThread client) {
            this.client = client;
        }

        /**
         * Cancels the task
         */
        public void cancelTask(){
            running = false;
        }

        @Override
        public void run() {
            if (running) {
                try {
                    client.listen();
                } catch (IOException e) {
                    e.printStackTrace();
                    running = false;
                }
            }
        }

    }

    /**
     * The task for the bungee socket server
     */
    static class ServerTask implements Runnable {

        /**
         * The server that is running this task
         */
        @NotNull
        private final BungeeSocketServer server;
        /**
         * Whether the task is running
         */
        private boolean running = true;

        /**
         * Create the task
         *
         * @param server the server that needs to listen to connections
         */
        ServerTask(@NotNull BungeeSocketServer server) {
            this.server = server;
        }

        /**
         * Cancels the task
         */
        public void cancelTask(){
            running = false;
        }

        @Override
        public void run() {
             if (running) {
                 server.run();
             }
        }

    }

}

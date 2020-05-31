package com.starfishst.test.sockets.server;

import com.starfishst.core.utils.sockets.messaging.handlers.ResponseGiver;
import com.starfishst.core.utils.sockets.messaging.handlers.type.DisconnectHandler;
import com.starfishst.core.utils.sockets.messaging.handlers.type.PingHandler;
import com.starfishst.core.utils.sockets.server.ClientThread;
import com.starfishst.core.utils.sockets.server.Server;
import com.starfishst.core.utils.time.Time;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public class TestServer {

  public static void main(String[] args) throws IOException {
    Server server =
        new Server(3000, Time.fromString("15s")) {
          @Override
          public void startClientTask(@NotNull ClientThread client) {
            while (true) {
              try {
                client.listen();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }
        };
    ResponseGiver.addHandler(new DisconnectHandler(server));
    ResponseGiver.addHandler(new PingHandler());
    while (true) {
      server.run();
    }
  }
}

package com.starfishst.test.sockets.client;

import com.starfishst.core.utils.sockets.client.Client;
import com.starfishst.core.utils.sockets.messaging.handlers.ResponseGiver;
import com.starfishst.core.utils.sockets.messaging.handlers.type.DisconnectedHandler;
import com.starfishst.core.utils.sockets.messaging.type.request.PingRequest;
import java.io.IOException;

public class TestClient {

  public static void main(String[] args) throws IOException {
    Client client = new Client("localhost", 3000);
    ResponseGiver.addHandler(new DisconnectedHandler(client));
    System.out.println(client.sendRequest(new PingRequest()).getData().getOrDefault("end", "0"));
    while (true) {
      client.listen();
    }
  }
}

package com.starfishst.test.http;

import com.starfishst.core.utils.http.Request;
import java.io.IOException;
import java.util.HashMap;

/** Sends a request to mojang to test {@link com.starfishst.core.utils.http.Request} */
public class HttpRequestTest {

  public static void main(String[] args) throws IOException {
    String route = "https://api.mojang.com/users/profiles/minecraft/Selfie";
    Request request = new Request(route, "GET", new HashMap<>());
    System.out.println(request.submit());
    request.disconnect();
  }
}

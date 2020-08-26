package com.starfishst.utils.gson;

import com.starfishst.core.utils.http.Request;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/** This object represents a http json request. Just like its extended
 * class, this heads to make request to an http to get certain objects.
 */
public class JsonRequest<O> extends Request {

  /**
   * Creates the request
   *
   * @param route the route that the request should be send
   * @param method the method that the request should be send
   * @param headers the headers to append to the request
   * @throws IOException if the connection could not be opened
   */
  public JsonRequest(
      @NotNull String route, @NotNull String method, @NotNull HashMap<String, String> headers)
      throws IOException {
    super(route, method, headers);
  }

  /**
   * Reads the json response as the desired object. This method uses {@link com.google.gson.Gson#fromJson(Reader, Type)}
   *
   * @return the object O
   * @throws IOException if the connection goes wrong
   */
  @NotNull
  public O read() throws IOException {
    return GsonProvider.GSON.fromJson(
        submit(),
        ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
  }

  /**
   * Get the headers that should be appended to the request
   *
   * @return the headers
   */
  @Override
  public @NotNull HashMap<String, String> getHeaders() {
    super.getHeaders().put("Content-Type", "application/json");
    return super.getHeaders();
  }
}

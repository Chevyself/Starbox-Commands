package com.starfishst.core.utils.sockets.messaging.handlers;

import com.starfishst.core.utils.sockets.exception.IllegalRequestException;
import com.starfishst.core.utils.sockets.exception.RequestHandlerNotFoundException;
import com.starfishst.core.utils.sockets.exception.SocketException;
import com.starfishst.core.utils.sockets.messaging.IMessenger;
import com.starfishst.core.utils.sockets.messaging.SocketRequest;
import com.starfishst.core.utils.sockets.messaging.SocketResponse;
import java.util.ArrayList;
import java.util.List;

import com.starfishst.core.utils.sockets.messaging.handlers.type.PingHandler;
import org.jetbrains.annotations.NotNull;

/** This handles the requests that a socket receives */
public class ResponseGiver {

  /** A list of handlers to handle different type of requests */
  @NotNull private static final List<RequestHandler> handlers = new ArrayList<>();

  static {
    handlers.add(new PingHandler());
  }

  /**
   * Get the response for certain request
   *
   * @param request the request waiting for a response
   * @param messenger the messenger that caught the request
   * @return the response
   * @throws SocketException in case that the request is illegal or there's no handler for it
   */
  @NotNull
  public static SocketResponse getResponse(@NotNull SocketRequest request, @NotNull IMessenger messenger) throws SocketException {
    return getHandler(request).getResponse(request, messenger);
  }

  /**
   * Get the handler matching the needed request
   *
   * @param request to get the method that the handler must match
   * @return the handler if found
   * @throws SocketException in case that the request is illegal or there's no handler for it
   */
  @NotNull
  public static RequestHandler getHandler(@NotNull SocketRequest request) throws SocketException {
    if (request.getData().containsKey("method")) {
      return getHandler(request.getData().get("method"));
    } else {
      throw new IllegalRequestException(request);
    }
  }

  /**
   * Get the handler matching the needed method
   *
   * @param method the method that the handler must match
   * @return the handler if found
   * @throws RequestHandlerNotFoundException if the handler is not found
   */
  @NotNull
  public static RequestHandler getHandler(@NotNull String method)
      throws RequestHandlerNotFoundException {
    return handlers.stream()
        .filter(handler -> handler.method().equalsIgnoreCase(method))
        .findFirst()
        .orElseThrow(() -> new RequestHandlerNotFoundException(method));
  }

  /**
   * Adds a handler to the list of handlers
   *
   * @param handler to add
   */
  public static void addHandler(@NotNull RequestHandler handler) {
    handlers.add(handler);
  }

  /**
   * Removes the handlers that match the method
   *
   * @param method the method to match
   */
  public static void removeHandler(@NotNull String method){
    handlers.removeIf(handler -> handler.method().equalsIgnoreCase(method));
  }

  /**
   * Removes a handler
   *
   * @param handler to remove
   */
  public static void removeHandler(@NotNull RequestHandler handler){
    handlers.remove(handler);
  }
}

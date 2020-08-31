package com.starfishst.core.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Makes a request to an http server */
public class Request {

  /** The route to send the request to */
  @NotNull private final String route;
  /** The headers to append to the request */
  @NotNull private final HashMap<String, String> headers;
  /** The connection of the request */
  @NotNull private final HttpURLConnection connection;
  /** The method in which the request should be send */
  @NotNull private String method;
  /** The string that the server responded with */
  @Nullable private String line;
  /** The response code that the server send */
  private int responseCode;

  /**
   * Creates the request
   *
   * @param route the route that the request should be send
   * @param method the method that the request should be send
   * @param headers the headers to append to the request
   * @throws IOException if the connection could not be opened
   */
  public Request(
      @NotNull String route, @NotNull String method, @NotNull HashMap<String, String> headers)
      throws IOException {
    this.route = route;
    this.method = method;
    this.headers = headers;
    this.connection = this.startConnection();
    this.appendHeaders();
  }

  /**
   * Starts the connection
   *
   * @return the connection that has started
   * @throws IOException if the connection could not be opened
   */
  @NotNull
  private HttpURLConnection startConnection() throws IOException {
    URL url = new URL(this.route);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    this.appendMethod(connection);
    return connection;
  }

  /**
   * Submits the request to the server
   *
   * @return the request line
   * @throws IOException if the connection fails to give a stream
   */
  @NotNull
  public String submit() throws IOException {
    if (this.line == null) {
      this.responseCode = this.connection.getResponseCode();
      InputStream stream;
      if (this.isError()) {
        stream = this.connection.getErrorStream();
      } else {
        stream = this.connection.getInputStream();
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      String line = reader.readLine();
      reader.close();
      this.line = line;
    }
    return this.line;
  }

  /** Disconnects the connection */
  public void disconnect() {
    this.connection.disconnect();
  }

  /**
   * Appends the method to the connection
   *
   * @param connection the connection to append the method
   */
  private void appendMethod(URLConnection connection) {
    switch (this.method) {
      case "GET":
        connection.setDoInput(true);
        break;
      case "POST":
      case "PUT":
        connection.setDoOutput(true);
        break;
      default:
        connection.setRequestProperty("X-HTTP-Method-Override", this.method);
        connection.setDoOutput(true);
        this.method = "POST";
        break;
    }
  }

  /** Appends the headers to the request */
  private void appendHeaders() {
    this.getHeaders().forEach(this.connection::setRequestProperty);
  }

  /**
   * Get the response code of the request
   *
   * @return the code
   */
  public int getResponseCode() {
    return responseCode;
  }

  /**
   * Get if the request ended with an error
   *
   * @return true if it is an error
   */
  public boolean isError() {
    return getResponseCode() >= 400;
  }

  /**
   * Get the headers that should be appended to the request
   *
   * @return the headers
   */
  @NotNull
  public HashMap<String, String> getHeaders() {
    return headers;
  }
}

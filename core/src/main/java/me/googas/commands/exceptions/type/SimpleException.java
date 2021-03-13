package me.googas.commands.exceptions.type;

import lombok.NonNull;

/** Represent a simple exception to easily create messages */
public class SimpleException extends Exception {

  public SimpleException() {
    super();
  }

  public SimpleException(@NonNull String message) {
    super(message);
  }

  public SimpleException(String message, @NonNull Throwable cause) {
    super(message, cause);
  }

  public SimpleException(@NonNull Throwable cause) {
    super(cause);
  }
}

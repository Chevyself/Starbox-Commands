package com.starfishst.utils.events;

import com.starfishst.core.exceptions.type.SimpleRuntimeException;
import org.jetbrains.annotations.NotNull;

/** Thrown when a listener cannot be registered */
public class ListenerRegistrationException extends SimpleRuntimeException {

  /**
   * Throw a simple runtime exception
   *
   * @param message the message
   */
  public ListenerRegistrationException(@NotNull String message) {
    super(message);
  }
}

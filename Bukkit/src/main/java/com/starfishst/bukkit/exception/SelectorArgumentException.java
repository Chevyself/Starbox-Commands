package com.starfishst.bukkit.exception;

import com.starfishst.core.exceptions.ArgumentProviderException;
import org.jetbrains.annotations.NotNull;

/** An exception thrown when an selector gets an argument that is incorrect */
public class SelectorArgumentException extends ArgumentProviderException {

  /**
   * Throw a simple exception
   *
   * @param message the message
   */
  public SelectorArgumentException(@NotNull String message) {
    super(message);
  }
}

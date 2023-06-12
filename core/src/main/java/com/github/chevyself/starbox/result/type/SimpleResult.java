package com.github.chevyself.starbox.result.type;

import com.github.chevyself.starbox.result.Result;
import lombok.NonNull;

/** Represents a result from a command execution which displays a message to the command sender. */
public class SimpleResult implements Result {

  @NonNull private final String message;

  /**
   * Create the result.
   *
   * @param message the message to display
   */
  public SimpleResult(@NonNull String message) {
    this.message = message;
  }

  @NonNull
  public String getMessage() {
    return message;
  }
}

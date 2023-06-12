package com.github.chevyself.starbox.result.type;

import lombok.Getter;
import lombok.NonNull;

/**
 * Represents that the command execution resulted in an exception.
 */
public abstract class ExceptionResult extends SimpleResult {

  @NonNull @Getter protected final Throwable exception;

  /**
   * Create the result.
   *
   * @param exception the exception thrown
   */
  public ExceptionResult(@NonNull Throwable exception) {
    super(
        exception.getMessage() == null
            ? exception.getClass().getSimpleName()
            : exception.getMessage());
    this.exception = exception;
  }

  @NonNull
  public String getMessage() {
    return exception.getClass().getSimpleName() + ", e: " + exception.getMessage();
  }
}

package com.github.chevyself.starbox.result;

import lombok.Getter;
import lombok.NonNull;

public abstract class ExceptionResult implements StarboxResult {

  @NonNull @Getter private final Throwable exception;

  public ExceptionResult(@NonNull Throwable exception) {
    this.exception = exception;
  }

  @NonNull
  public String getMessage() {
    return exception.getClass().getSimpleName() + ", e: " + exception.getMessage();
  }
}

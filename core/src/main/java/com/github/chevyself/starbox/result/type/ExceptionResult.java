package com.github.chevyself.starbox.result.type;

import com.github.chevyself.starbox.result.Result;
import lombok.Getter;
import lombok.NonNull;

public abstract class ExceptionResult extends SimpleResult {

  @NonNull @Getter protected final Throwable exception;

  public ExceptionResult(@NonNull Throwable exception) {
    super(exception.getMessage() == null ? exception.getClass().getSimpleName() : exception.getMessage());
    this.exception = exception;
  }

  @NonNull
  public String getMessage() {
    return exception.getClass().getSimpleName() + ", e: " + exception.getMessage();
  }
}

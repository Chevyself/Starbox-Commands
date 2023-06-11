package com.github.chevyself.starbox.result;

import lombok.NonNull;

public class InternalExceptionResult extends ExceptionResult {

  public InternalExceptionResult(@NonNull Exception exception) {
    super(exception);
  }
}

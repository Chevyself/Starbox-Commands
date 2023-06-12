package com.github.chevyself.starbox.result.type;

import lombok.NonNull;

public class InternalExceptionResult extends ExceptionResult {

  public InternalExceptionResult(@NonNull Exception exception) {
    super(exception);
  }
}

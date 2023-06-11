package com.github.chevyself.starbox.result;

import com.github.chevyself.starbox.exceptions.type.StarboxException;
import lombok.NonNull;

public class ArgumentExceptionResult extends ExceptionResult {

  public ArgumentExceptionResult(@NonNull StarboxException exception) {
    super(exception);
  }

  @Override
  public @NonNull String getMessage() {
    return this.exception.getMessage();
  }
}

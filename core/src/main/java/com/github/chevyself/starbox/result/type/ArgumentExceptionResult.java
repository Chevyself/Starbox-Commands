package com.github.chevyself.starbox.result.type;

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

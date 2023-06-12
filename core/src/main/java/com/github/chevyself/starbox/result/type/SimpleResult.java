package com.github.chevyself.starbox.result.type;

import com.github.chevyself.starbox.result.Result;
import lombok.NonNull;

public class SimpleResult implements Result {

  @NonNull private final String message;

  public SimpleResult(@NonNull String message) {
    this.message = message;
  }

  @NonNull
  public String getMessage() {
    return message;
  }
}

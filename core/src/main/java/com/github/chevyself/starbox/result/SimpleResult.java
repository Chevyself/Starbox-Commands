package com.github.chevyself.starbox.result;

import lombok.NonNull;

public class SimpleResult implements StarboxResult {

  @NonNull private final String message;

  public SimpleResult(@NonNull String message) {
    this.message = message;
  }

  @NonNull
  public String getMessage() {
    return message;
  }
}

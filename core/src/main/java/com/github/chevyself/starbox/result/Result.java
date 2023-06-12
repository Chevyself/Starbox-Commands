package com.github.chevyself.starbox.result;

import lombok.NonNull;

public interface Result {

  static Result of(@NonNull String string) {
    return new SimpleResult(string);
  }
}

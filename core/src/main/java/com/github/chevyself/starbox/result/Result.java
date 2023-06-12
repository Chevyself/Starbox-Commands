package com.github.chevyself.starbox.result;

import com.github.chevyself.starbox.result.type.SimpleCooldownResult;
import com.github.chevyself.starbox.result.type.SimpleResult;
import java.time.Duration;
import lombok.NonNull;

public interface Result {

  @NonNull
  static Result of(@NonNull String string) {
    return new SimpleResult(string);
  }

  @NonNull
  static Result of(@NonNull String string, @NonNull Duration cooldown) {
    return new SimpleCooldownResult(string, cooldown);
  }
}

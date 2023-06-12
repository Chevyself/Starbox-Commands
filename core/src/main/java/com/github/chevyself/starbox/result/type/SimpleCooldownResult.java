package com.github.chevyself.starbox.result.type;

import com.github.chevyself.starbox.result.CooldownResult;
import java.time.Duration;
import lombok.Getter;
import lombok.NonNull;

public class SimpleCooldownResult extends SimpleResult implements CooldownResult {

  @NonNull
  @Getter
  private final Duration cooldown;

  public SimpleCooldownResult(@NonNull String message, @NonNull Duration cooldown) {
    super(message);
    this.cooldown = cooldown;
  }
}

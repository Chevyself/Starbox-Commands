package me.googas;

import com.github.chevyself.starbox.result.CooldownResult;
import com.github.chevyself.starbox.result.SimpleResult;
import java.time.Duration;
import lombok.Getter;
import lombok.NonNull;

public class SimpleCooldownResult extends SimpleResult implements CooldownResult {

  @NonNull @Getter private final Duration cooldown;

  public SimpleCooldownResult(@NonNull String message, @NonNull Duration cooldown) {
    super(message);
    this.cooldown = cooldown;
  }
}

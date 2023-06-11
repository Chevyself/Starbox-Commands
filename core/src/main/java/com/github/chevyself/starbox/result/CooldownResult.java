package com.github.chevyself.starbox.result;

import java.time.Duration;
import lombok.NonNull;

public interface CooldownResult extends Result {

  @NonNull
  Duration getCooldown();
}

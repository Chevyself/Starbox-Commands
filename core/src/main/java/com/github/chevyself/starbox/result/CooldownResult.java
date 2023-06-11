package com.github.chevyself.starbox.result;

import java.time.Duration;
import lombok.NonNull;

public interface CooldownResult extends StarboxResult {

  @NonNull
  Duration getCooldown();
}

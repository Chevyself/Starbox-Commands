package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.StarboxCooldownManager;
import com.github.chevyself.starbox.system.context.CommandContext;
import java.time.Duration;
import lombok.NonNull;

/** Implementation of {@link CommandManager} for system commands. */
public class CooldownManager implements StarboxCooldownManager<CommandContext> {

  @NonNull private final Duration duration;
  private long millis;

  /**
   * Create the manager.
   *
   * @param duration the amount of time that the sender has to wait to execute the command again
   */
  public CooldownManager(@NonNull Duration duration) {
    this.duration = duration;
  }

  @Override
  public boolean hasCooldown(@NonNull CommandContext context) {
    return millis > System.currentTimeMillis();
  }

  @Override
  public Duration getTimeLeft(@NonNull CommandContext context) {
    return Duration.ofMillis(millis - System.currentTimeMillis());
  }

  @Override
  public void refresh(@NonNull CommandContext context) {
    this.millis = (System.currentTimeMillis() + duration.toMillis());
  }
}

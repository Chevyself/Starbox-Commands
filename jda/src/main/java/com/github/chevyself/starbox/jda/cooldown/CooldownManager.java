package com.github.chevyself.starbox.jda.cooldown;

import com.github.chevyself.starbox.StarboxCooldownManager;
import com.github.chevyself.starbox.jda.annotations.Command;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.time.TimeUtil;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;

/** This is an implementation of {@link CooldownManager} for the JDA module. */
public class CooldownManager implements StarboxCooldownManager<CommandContext> {

  @NonNull private final Map<Long, Long> map = new HashMap<>();
  @NonNull private final Duration duration;

  /**
   * Constructs the manager with the specific time to cooldown.
   *
   * @param duration the time that the command needs to cooldown
   */
  protected CooldownManager(@NonNull Duration duration) {
    this.duration = duration;
  }

  /**
   * Returns the manager object based on the annotation. If the annotation has a valid time (this
   * means that it is not {@link Duration#isZero()}) a {@link Optional} will be wrapping the
   * manager.
   *
   * @param annotation the annotation to provide the time for the manager
   * @return a {@link Optional} which might be wrapping the manager
   */
  @NonNull
  public static Optional<CooldownManager> of(@NonNull Command annotation) {
    Duration duration = TimeUtil.durationOf(annotation.cooldown());
    return Optional.ofNullable(duration.isZero() ? null : new CooldownManager(duration));
  }

  private long getMillis(@NonNull CommandContext context) {
    return map.getOrDefault(context.getSender().getIdLong(), 0L);
  }

  @Override
  public boolean hasCooldown(@NonNull CommandContext context) {
    return this.getMillis(context) > System.currentTimeMillis();
  }

  @Override
  public Duration getTimeLeft(@NonNull CommandContext context) {
    long millis = this.getMillis(context) - System.currentTimeMillis();
    return Duration.ofMillis(millis < 0 ? 0 : millis);
  }

  @Override
  public void refresh(@NonNull CommandContext context) {
    map.put(context.getSender().getIdLong(), System.currentTimeMillis() + duration.toMillis());
  }
}

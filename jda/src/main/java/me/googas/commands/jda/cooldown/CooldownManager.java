package me.googas.commands.jda.cooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.StarboxCooldownManager;
import me.googas.commands.jda.annotations.Command;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.time.Time;

public class CooldownManager implements StarboxCooldownManager<CommandContext> {

  @NonNull private final Map<Long, Long> map = new HashMap<>();
  @NonNull private final Time time;

  protected CooldownManager(@NonNull Time time) {
    this.time = time;
  }

  @NonNull
  public static Optional<CooldownManager> of(@NonNull Command annotation) {
    Time time = Time.of(annotation.cooldown());
    return Optional.ofNullable(time.isZero() ? null : new CooldownManager(time));
  }

  private long getMillis(@NonNull CommandContext context) {
    return map.getOrDefault(context.getSender().getIdLong(), 0L);
  }

  @Override
  public boolean hasCooldown(@NonNull CommandContext context) {
    return this.getMillis(context) > System.currentTimeMillis();
  }

  @Override
  public @NonNull Time getTimeLeft(@NonNull CommandContext context) {
    long millis = this.getMillis(context) - System.currentTimeMillis();
    return Time.ofMillis(millis < 0 ? 0 : millis, true);
  }

  @Override
  public void refresh(@NonNull CommandContext context) {
    map.put(context.getSender().getIdLong(), System.currentTimeMillis() + time.toMillisRound());
  }
}

package me.googas.commands.jda.cooldown;

import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.time.Time;

public class UserCooldownManager extends JdaCooldownManager {

  @NonNull private final Map<Long, Long> map = new HashMap<>();

  protected UserCooldownManager(@NonNull Time time) {
    super(time);
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
    return Time.ofMillis(this.getMillis(context) - System.currentTimeMillis(), true);
  }

  @Override
  public void refresh(@NonNull CommandContext context) {
    map.put(context.getSender().getIdLong(), System.currentTimeMillis() + time.toMillisRound());
  }
}

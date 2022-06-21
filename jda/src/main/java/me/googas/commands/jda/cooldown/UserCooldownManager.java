package me.googas.commands.jda.cooldown;

import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.permissions.Permit;
import me.googas.commands.time.Time;

public class UserCooldownManager extends JdaCooldownManager {

  @NonNull private final Map<Long, Long> map = new HashMap<>();

  protected UserCooldownManager(@NonNull Time time, Permit permission) {
    super(time, permission);
  }

  private long getMillis(@NonNull CommandContext context) {
    return map.getOrDefault(context.getSender().getIdLong(), 0L);
  }

  @Override
  public boolean hasCooldown(@NonNull CommandContext context) {
    boolean hasPermission = this.permission != null && context.getManager().getPermissionChecker().checkPermission(context, this.permission) == null;
    return this.getMillis(context) > System.currentTimeMillis() && !hasPermission;
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

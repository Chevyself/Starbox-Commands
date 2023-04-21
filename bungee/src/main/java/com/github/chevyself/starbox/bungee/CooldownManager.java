package com.github.chevyself.starbox.bungee;

import com.github.chevyself.starbox.StarboxCooldownManager;
import com.github.chevyself.starbox.bungee.annotations.Cooldown;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.time.TimeUtil;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/** Implementation for the 'Bungee' module. */
public class CooldownManager implements StarboxCooldownManager<CommandContext> {

  @NonNull private final Map<UUID, Long> map = new HashMap<>();

  @NonNull private final String permission;
  @NonNull private final Duration duration;

  /**
   * Create the manager.
   *
   * @param permission the permission that overrides the cooldown
   * @param duration the time that a sender needs to wait before executing the command again
   */
  public CooldownManager(@NonNull String permission, @NonNull Duration duration) {
    this.permission = permission;
    this.duration = duration;
  }

  /**
   * Create a manager based on the annotation.
   *
   * @param cooldown the cooldown annotation
   * @return if {@link Duration#isZero()} an empty optional will be returned else a new manager will
   *     be created
   */
  @NonNull
  public static Optional<CooldownManager> of(@NonNull Cooldown cooldown) {
    Duration duration = TimeUtil.durationOf(cooldown.time());
    return Optional.ofNullable(
        duration.isZero() ? null : new CooldownManager(cooldown.permission(), duration));
  }

  private long getMillis(@NonNull CommandContext context) {
    CommandSender sender = context.getSender();
    return sender instanceof ProxiedPlayer
        ? map.getOrDefault(((ProxiedPlayer) sender).getUniqueId(), 0L)
        : 0;
  }

  @Override
  public boolean hasCooldown(@NonNull CommandContext context) {
    boolean hasPermission =
        this.permission.isEmpty() || (context.getSender().hasPermission(this.permission));
    return this.getMillis(context) > System.currentTimeMillis() && !hasPermission;
  }

  @Override
  public Duration getTimeLeft(@NonNull CommandContext context) {
    long millis = this.getMillis(context) - System.currentTimeMillis();
    return Duration.ofMillis(millis < 0 ? 0 : millis);
  }

  @Override
  public void refresh(@NonNull CommandContext context) {
    CommandSender sender = context.getSender();
    if (sender instanceof ProxiedPlayer) {
      map.put(
          ((ProxiedPlayer) sender).getUniqueId(), System.currentTimeMillis() + duration.toMillis());
    }
  }
}

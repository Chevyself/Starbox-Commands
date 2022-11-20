package chevyself.github.commands.bungee;

import chevyself.github.commands.StarboxCooldownManager;
import chevyself.github.commands.bungee.annotations.Cooldown;
import chevyself.github.commands.bungee.context.CommandContext;
import chevyself.github.commands.time.Time;
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
  @NonNull private final Time time;

  /**
   * Create the manager.
   *
   * @param permission the permission that overrides the cooldown
   * @param time the time that a sender needs to wait before executing the command again
   */
  public CooldownManager(@NonNull String permission, @NonNull Time time) {
    this.permission = permission;
    this.time = time;
  }

  /**
   * Create a manager based on the annotation.
   *
   * @param cooldown the cooldown annotation
   * @return if {@link Time#isZero()} an empty optional will be returned else a new manager will be
   *     created
   */
  @NonNull
  public static Optional<CooldownManager> of(@NonNull Cooldown cooldown) {
    Time time = Time.of(cooldown.time());
    return Optional.ofNullable(
        time.isZero() ? null : new CooldownManager(cooldown.permission(), time));
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
  public @NonNull Time getTimeLeft(@NonNull CommandContext context) {
    long millis = this.getMillis(context) - System.currentTimeMillis();
    return Time.ofMillis(millis < 0 ? 0 : millis, true);
  }

  @Override
  public void refresh(@NonNull CommandContext context) {
    CommandSender sender = context.getSender();
    if (sender instanceof ProxiedPlayer) {
      map.put(
          ((ProxiedPlayer) sender).getUniqueId(),
          System.currentTimeMillis() + time.toMillisRound());
    }
  }
}

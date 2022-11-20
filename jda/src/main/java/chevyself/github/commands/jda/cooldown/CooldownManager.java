package chevyself.github.commands.jda.cooldown;

import chevyself.github.commands.StarboxCooldownManager;
import chevyself.github.commands.jda.annotations.Command;
import chevyself.github.commands.jda.context.CommandContext;
import chevyself.github.commands.time.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;

/** This is an implementation of {@link CooldownManager} for the JDA module. */
public class CooldownManager implements StarboxCooldownManager<CommandContext> {

  @NonNull private final Map<Long, Long> map = new HashMap<>();
  @NonNull private final Time time;

  /**
   * Constructs the manager with the specific time to cooldown.
   *
   * @param time the time that the command needs to cooldown
   */
  protected CooldownManager(@NonNull Time time) {
    this.time = time;
  }

  /**
   * Returns the manager object based on the annotation. If the annotation has a valid time (this
   * means that it is not {@link Time#isZero()}) a {@link Optional} will be wrapping the manager.
   *
   * @param annotation the annotation to provide the time for the manager
   * @return a {@link Optional} which might be wrapping the manager
   */
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

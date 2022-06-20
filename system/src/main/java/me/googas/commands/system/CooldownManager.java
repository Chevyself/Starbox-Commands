package me.googas.commands.system;

import lombok.NonNull;
import me.googas.commands.StarboxCooldownManager;
import me.googas.commands.system.context.CommandContext;
import me.googas.commands.time.Time;

/** Implementation of {@link CommandManager} for system commands. */
public class CooldownManager implements StarboxCooldownManager<CommandContext> {

  private long millis;
  @NonNull private final Time time;

  /**
   * Create the manager.
   *
   * @param time the amount of time that the sender has to wait to execute the command again
   */
  public CooldownManager(@NonNull Time time) {
    this.time = time;
  }

  @Override
  public boolean hasCooldown(@NonNull CommandContext context) {
    return millis > System.currentTimeMillis();
  }

  @Override
  public @NonNull Time getTimeLeft(@NonNull CommandContext context) {
    return Time.ofMillis(millis - System.currentTimeMillis(), true);
  }

  @Override
  public void refresh(@NonNull CommandContext context) {
    this.millis = (System.currentTimeMillis() + time.toMillisRound());
  }
}

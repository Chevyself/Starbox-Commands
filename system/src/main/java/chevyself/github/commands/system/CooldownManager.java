package chevyself.github.commands.system;

import chevyself.github.commands.StarboxCooldownManager;
import chevyself.github.commands.system.context.CommandContext;
import chevyself.github.commands.time.Time;
import lombok.NonNull;

/** Implementation of {@link CommandManager} for system commands. */
public class CooldownManager implements StarboxCooldownManager<CommandContext> {

  @NonNull private final Time time;
  private long millis;

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

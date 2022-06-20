package me.googas.commands.jda.cooldown;

import lombok.NonNull;
import me.googas.commands.StarboxCooldownManager;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.time.Time;

public abstract class JdaCooldownManager implements StarboxCooldownManager<CommandContext> {

  @NonNull protected final Time time;

  protected JdaCooldownManager(@NonNull Time time) {
    this.time = time;
  }
}

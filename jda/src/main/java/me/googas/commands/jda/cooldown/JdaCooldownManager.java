package me.googas.commands.jda.cooldown;

import lombok.NonNull;
import me.googas.commands.StarboxCooldownManager;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.permissions.Permit;
import me.googas.commands.time.Time;

/** Represents a cooldown manager for the JDA module. */
public abstract class JdaCooldownManager implements StarboxCooldownManager<CommandContext> {

  @NonNull protected final Time time;
  protected final Permit permission;

  /**
   * Create the manager.
   *
   * @param time the time that the command needs to cooldown
   * @param permission the permission which users may have to not require cooldown
   */
  protected JdaCooldownManager(@NonNull Time time, Permit permission) {
    this.time = time;
    this.permission = permission;
  }
}

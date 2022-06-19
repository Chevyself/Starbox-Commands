package me.googas.commands;

import lombok.NonNull;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.time.Time;

/**
 * Manages the cooldown of commands.
 *
 * Cooldown is basically the time that a sender has to wait until it can execute a command again.
 *
 * @param <C> the context that executes the command
 */
public interface CooldownManager<C extends StarboxCommandContext> {

  /**
   * Check whether a context has cooldown.
   *
   * @param context the context that is running the command
   * @return true if the context is still on cooldown
   */
  boolean hasCooldown(@NonNull C context);

  /**
   * Get the amount of time that the context has to wait until it can run the command again.
   *
   * @param context the context that is running the command
   * @return the amount of time
   */
  @NonNull
  Time getTimeLeft(@NonNull C context);

  /**
   * Refreshes the time that a sender has to wait to execute the command again.
   *
   * @param context the context that is running the command
   */
  void refresh(@NonNull C context);
}

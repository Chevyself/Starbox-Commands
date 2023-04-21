package com.github.chevyself.starbox;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import java.time.Duration;
import lombok.NonNull;

/**
 * Manages the cooldown of commands.
 *
 * <p>Cooldown is basically the time that a sender has to wait until it can execute a command again.
 *
 * @param <C> the context that executes the command
 */
public interface StarboxCooldownManager<C extends StarboxCommandContext> {

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
  Duration getTimeLeft(@NonNull C context);

  /**
   * Refreshes the time that a sender has to wait to execute the command again.
   *
   * @param context the context that is running the command
   */
  void refresh(@NonNull C context);
}

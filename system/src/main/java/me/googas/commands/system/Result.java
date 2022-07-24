package me.googas.commands.system;

import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

/**
 * This is the implementation for {@link SystemResult}. This includes a single {@link String} which
 * will be printed for the sender.
 *
 * <p>Exceptions will show a simple {@link SystemResult} message and the stack trace will be
 * printed.
 */
public class Result implements SystemResult {
  @NonNull private final String message;
  @Getter private boolean cooldown;

  /**
   * Create a result with a message to print.
   *
   * @param message the message to print
   */
  public Result(@NonNull String message) {
    this.message = message;
  }

  /** Create an empty result with no message to print. */
  public Result() {
    this("");
  }

  @Override
  public @NonNull Optional<String> getMessage() {
    return Optional.of(message);
  }

  @Override
  public @NonNull SystemResult setCooldown(boolean apply) {
    this.cooldown = apply;
    return this;
  }
}

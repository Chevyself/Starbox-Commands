package com.github.chevyself.starbox.middleware;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.result.StarboxResult;
import java.util.Optional;
import lombok.NonNull;

/**
 * This represents an instance which is executed before and after a command. This means: cooldown
 * check, permissions check, etc. Anything that you want to run before or after a command can be
 * done using this.
 *
 * <p>If the middleware runs before the command and returns a {@link StarboxResult} the command will
 * not run
 *
 * @param <T> the context of the command
 */
public interface Middleware<T extends StarboxCommandContext> {

  /**
   * Runs the middleware before the command.
   *
   * @param context the context that is going to run the command
   * @return if the middleware returns a {@link StarboxResult} will not run the command
   */
  @NonNull
  default Optional<StarboxResult> next(@NonNull T context) {
    return Optional.empty();
  }

  /**
   * Runs the middleware after the command.
   *
   * @param context the context that ran the command
   * @param result result returned by the command
   */
  default void next(@NonNull T context, StarboxResult result) {}
}

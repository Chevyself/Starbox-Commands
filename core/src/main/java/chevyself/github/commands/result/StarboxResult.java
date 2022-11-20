package chevyself.github.commands.result;

import java.util.Optional;
import lombok.NonNull;

/**
 * When a command is executed it will give a result.
 *
 * <pre>{@code
 * public class AClass {
 *     &#64;Command(aliases = "Hello")
 *     public Result aCommand() {
 *        return new Result("World!");
 *     }
 * }
 * }</pre>
 *
 * <p>This will be used to inform the command sender the output of the command
 */
public interface StarboxResult {

  /**
   * Get the result message.
   *
   * @return a {@link Optional} instance wrapping the nullable message
   */
  @NonNull
  Optional<String> getMessage();

  /**
   * Check whether this result should apply cooldown to the context.
   *
   * @return true if this result should apply cooldown
   */
  default boolean isCooldown() {
    return false;
  }

  /**
   * Set whether this result should apply cooldown to the context.
   *
   * @param apply the new value
   * @return this same result instance
   */
  @NonNull
  default StarboxResult setCooldown(boolean apply) {
    return this;
  }
}

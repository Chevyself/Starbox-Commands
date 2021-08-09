package me.googas.commands.result;

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
 * This will be used to inform the command sender the output of the command
 */
public interface StarboxResult {

  /**
   * Get the result message.
   *
   * @return a {@link Optional} instance wrapping the nullable message
   */
  @NonNull
  Optional<String> getMessage();
}

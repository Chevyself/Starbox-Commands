package me.googas.commands.result;

/**
 * When a command is executed it will give a result:
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
   * Get the result message
   *
   * @return the result message
   */
  String getMessage();
}

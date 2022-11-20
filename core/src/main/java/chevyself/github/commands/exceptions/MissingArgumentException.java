package chevyself.github.commands.exceptions;

import chevyself.github.commands.ReflectCommand;
import chevyself.github.commands.annotations.Required;
import chevyself.github.commands.arguments.Argument;
import chevyself.github.commands.exceptions.type.StarboxException;
import lombok.NonNull;

/**
 * This exception is thrown by {@link ReflectCommand} when an {@link Argument} is missing from the
 * execution.
 *
 * <p>If you have a command with the next usage:
 *
 * <p>[prefix][command] [{@link Required} String name]
 *
 * <p>Let's suppose to have '/' as the prefix, the command 'hello', and the user runs:
 *
 * <p>/hello
 *
 * <p>An exception will be thrown as the {@link Required} argument is missing. The proper execution
 * of the command will be:
 *
 * <p>/hello world!
 */
public class MissingArgumentException extends StarboxException {

  /**
   * Create a simple exception with a simple message.
   *
   * @param message the message with the cause of the exception
   */
  public MissingArgumentException(@NonNull String message) {
    super(message);
  }
}

package me.googas.commands.exceptions;

import lombok.NonNull;
import me.googas.commands.exceptions.type.StarboxException;

/**
 * This exception is thrown by {@link me.googas.commands.ReflectCommand} when an {@link
 * me.googas.commands.arguments.Argument} is missing from the execution.
 *
 * <p>If you have a command with the next usage:
 *
 * <p>[prefix][command] [{@link me.googas.commands.annotations.Required} String name]
 *
 * <p>Let's suppose to have '/' as the prefix, the command 'hello', and the user runs:
 *
 * <p>/hello
 *
 * <p>An exception will be thrown as the {@link me.googas.commands.annotations.Required} argument is
 * missing. The proper execution of the command will be:
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

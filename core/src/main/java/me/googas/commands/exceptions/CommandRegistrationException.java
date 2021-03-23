package me.googas.commands.exceptions;

import lombok.NonNull;
import me.googas.commands.exceptions.type.SimpleRuntimeException;

/**
 * This command is thrown by the {@link me.googas.commands.EasyCommandManager} when it cannot
 * register an {@link me.googas.commands.EasyCommand}
 */
public class CommandRegistrationException extends SimpleRuntimeException {

  /**
   * Create a simple exception with a simple message
   *
   * @param message the message with the cause of the exception
   */
  public CommandRegistrationException(@NonNull String message) {
    super(message);
  }
}

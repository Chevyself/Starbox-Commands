package chevyself.github.commands.exceptions;

import chevyself.github.commands.StarboxCommand;
import chevyself.github.commands.StarboxCommandManager;
import chevyself.github.commands.exceptions.type.StarboxRuntimeException;
import lombok.NonNull;

/**
 * This command is thrown by the {@link StarboxCommandManager} when it cannot parseAndRegister an
 * {@link StarboxCommand}.
 */
public class CommandRegistrationException extends StarboxRuntimeException {

  /**
   * Create a simple exception with a simple message.
   *
   * @param message the message with the cause of the exception
   */
  public CommandRegistrationException(@NonNull String message) {
    super(message);
  }
}

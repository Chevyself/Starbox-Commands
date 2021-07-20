package me.googas.commands.system;

import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.result.StarboxResult;
import me.googas.commands.system.context.CommandContext;

/**
 * This is the implementation for {@link StarboxResult} to be used in the execution of {@link
 * me.googas.commands.system.SystemCommand}. This includes a single {@link String} which will be
 * printed for the sender.
 *
 * <p>If the {@link Result} from {@link
 * me.googas.commands.system.SystemCommand#execute(CommandContext)} is null or empty no message will
 * be shown to the sender.
 *
 * <p>Exceptions will show a simple {@link Result} message and the stack trace will be printed.
 */
public class Result implements StarboxResult {

  @NonNull @Getter private final String message;

  /**
   * Create a result with a message to print
   *
   * @param message the message to print
   */
  public Result(@NonNull String message) {
    this.message = message;
  }

  /** Create an empty result with no message to print */
  public Result() {
    this("");
  }
}

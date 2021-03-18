package me.googas.commands.result;

/**
 * A simple result given by a command
 *
 * @deprecated each module has different implementations which makes this unused
 */
public class SimpleResult implements IResult {

  /** A message sent by the execution of a command */
  private final String message;

  /**
   * Create a new instance of {@link SimpleResult} with a message with place holders
   *
   * @param message the message with place holders
   */
  protected SimpleResult(String message) {
    this.message = message;
  }

  /** Create a new instance of {@link SimpleResult} with no message; */
  protected SimpleResult() {
    this.message = null;
  }

  @Override
  public String getMessage() {
    return this.message;
  }
}

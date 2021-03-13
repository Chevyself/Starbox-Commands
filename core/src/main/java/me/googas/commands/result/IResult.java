package me.googas.commands.result;

/** The result is a message send by the command to the sender from it's execution */
public interface IResult {

  /**
   * Get the result message
   *
   * @return the result message
   */
  String getMessage();
}

package me.googas.commands.arguments;

/** This is the behaviour of the argument. */
public enum ArgumentBehaviour {
  /**
   * The argument is represented as a single word.
   *
   * <p>Example: -register [username] [password] Usage: -register Chevy notagoodpassword123
   */
  NORMAL,
  /**
   * The argument can be represented as a single or many words. In case that this needs more than
   * one word it requires quotation marks:
   *
   * <p>Example: -message [message] Usage: -message "Hello wold"
   */
  MULTIPLE,
  /**
   * This is an argument that accepts all the strings that come after its position.
   *
   * <p>Example: -numbers [number]... [another number] Usage: -numbers 1 2 3 4 5 6 7 8 9
   *
   * <ul>
   *   number: 1 2 3 4 5 6 7 8 9 another number: 2
   * </ul>
   *
   * This can be more useful at the end of the command
   */
  CONTINUOUS
}

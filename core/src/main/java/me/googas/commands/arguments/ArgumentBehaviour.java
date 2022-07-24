package me.googas.commands.arguments;

import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.util.JoinedString;
import me.googas.commands.util.Pair;
import me.googas.commands.util.Strings;

/** This is the behaviour of the argument. */
public enum ArgumentBehaviour {
  /**
   * The argument is represented as a single word.
   *
   * <p>Example: -register [username] [password] Usage: -register Chevy notagoodpassword123
   */
  NORMAL(
      ((argument, context, lastIndex) ->
          new Pair<>(context.getStrings()[argument.getPosition() + lastIndex], 0))),
  /**
   * The argument can be represented as a single or many words. In case that this needs more than
   * one word it requires quotation marks:
   *
   * <p>Example: -message [message] Usage: -message "Hello wold"
   */
  MULTIPLE(
      ((argument, context, lastIndex) -> {
        JoinedString joined =
            Strings.group(context.getStringsFrom(argument.getPosition() + lastIndex)).get(0);
        return new Pair<>(joined.getString(), joined.getSize());
      })),
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
  CONTINUOUS(
      ((argument, context, lastIndex) ->
          new Pair<>(Strings.join(context.getStringsFrom(argument.getPosition() + lastIndex)), 0)));

  @NonNull @Getter private final ArgumentBehaviour.StringProcessor stringProcessor;

  ArgumentBehaviour(@NonNull ArgumentBehaviour.StringProcessor stringProcessor) {
    this.stringProcessor = stringProcessor;
  }

  /** This interface helps to process the string for an argument. */
  public interface StringProcessor {

    /**
     * Get the processed string.
     *
     * @param argument the argument that requires the processed string
     * @param context the context of the command execution
     * @param lastIndex the last index of the command execution
     * @return a pair containing the string and the increase for the last index
     */
    @NonNull
    Pair<String, Integer> getString(
        @NonNull SingleArgument<?> argument, @NonNull StarboxCommandContext context, int lastIndex);
  }
}

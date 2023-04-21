package com.github.chevyself.starbox.flags;

import com.github.chevyself.starbox.util.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

/** Represents a flag which has been introduced in a command line. */
public final class FlagArgument implements StarboxFlag {

  @NonNull @Getter private final Option option;
  private final String value;

  /**
   * Create the flag.
   *
   * @param option the option that was registered in the command and was looking for this input
   * @param value the value which has the flag
   */
  public FlagArgument(@NonNull Option option, String value) {
    this.option = option;
    this.value = value;
  }

  /**
   * Parse the flags from the command line.
   *
   * @param options the list of options to check the flags
   * @param build whether the parsing should build the arguments inside quotation marks
   * @param strings the command line as a single string
   * @return the parser that got the flags
   * @see Parser#parse
   */
  @NonNull
  public static Parser parse(
      @NonNull Collection<? extends Option> options, boolean build, @NonNull String strings) {
    return FlagArgument.parse(options, build, strings.split(" "));
  }

  /**
   * Parse the flags from the command line.
   *
   * @param options the list of options to check the flags
   * @param build whether the parsing should build the arguments inside quotation marks
   * @param strings the command line as an array of strings
   * @return the parser that got the flags
   * @see Parser#parse
   */
  @NonNull
  public static Parser parse(
      @NonNull Collection<? extends Option> options, boolean build, @NonNull String... strings) {
    return new Parser(new ArrayList<>(Arrays.asList(strings)), options).parse(build);
  }

  /**
   * Parse the flags from the command line.
   *
   * @param options the list of options to check the flags
   * @param strings the command line as a single string
   * @return the parser that got the flags
   * @see Parser#parse
   */
  @NonNull
  public static Parser parse(
      @NonNull Collection<? extends Option> options, @NonNull String strings) {
    return FlagArgument.parse(options, true, strings);
  }

  /**
   * Parse the flags from the command line.
   *
   * @param options the list of options to check the flags
   * @param strings the command line as an array of strings
   * @return the parser that got the flags
   * @see Parser#parse(boolean)
   */
  @NonNull
  public static Parser parse(
      @NonNull Collection<? extends Option> options, @NonNull String... strings) {
    return FlagArgument.parse(options, true, strings);
  }

  @NonNull
  public Optional<String> getValue() {
    return Optional.ofNullable(value);
  }

  @Override
  public String toString() {
    return "ArgumentFlag{" + "option=" + option + ", value='" + value + '\'' + '}';
  }

  @Override
  public @NonNull Collection<String> getAliases() {
    return option.getAliases();
  }

  @Override
  public @NonNull String getDescription() {
    return option.getDescription();
  }

  /** This object contains the parsed elements from the command line. */
  public static final class Parser {

    @NonNull private static final String separator = "=";
    @NonNull @Getter private final List<String> arguments;
    @NonNull private final Collection<? extends Option> options;
    @NonNull @Getter private final List<FlagArgument> flags = new ArrayList<>();
    @NonNull private final StringBuilder valueBuilder = new StringBuilder();
    private boolean building = false;
    private Option toValue = null;

    private Parser(@NonNull List<String> arguments, @NonNull Collection<? extends Option> options) {
      this.arguments = arguments;
      this.options = options;
    }

    @NonNull
    private Parser parse() {
      return this.parse(true);
    }

    /**
     * Parse the command line looking for flags.
     *
     * <p>How 'build' works:
     *
     * <p>Let's take this line as an example: 'msg -u=Chevyself --message "Hello world" '
     *
     * <p>If it is set to false '"Hello world"' would not be used as a value for '--message' unless
     * {@link #arguments} has taken care of making arguments inside quotation marks as a single
     * argument.
     *
     * <p>If it is set to true it will work just as expected:
     *
     * <ul>
     *   <li>'-u' :: 'Chevyself'
     *   <li>'--message' :: 'Hello world'
     * </ul>
     *
     * @param build whether to construct strings that are inside quotation marks to serve as value
     *     for flags.
     * @return this same instance with parsed elements
     */
    @NonNull
    private Parser parse(boolean build) {
      List<String> argumentsCopy = new ArrayList<>(arguments);
      int left = 0;
      for (int i = 0; i < argumentsCopy.size(); i++) {
        String argument = argumentsCopy.get(i);
        if (toValue == null || (!building && this.isFlag(argument))) {
          Optional<? extends Option> optional = this.getOption(argument);
          if (optional.isPresent()) {
            Option option = optional.get();
            if (option.isValuable()) {
              if (argument.contains(Parser.separator)) {
                String[] split = argument.split(Parser.separator);
                String flagValue = split.length < 2 ? "" : split[1];
                if (flagValue.isEmpty() || (build && Strings.isStart(flagValue))) {
                  toValue = option;
                  building = Strings.isStart(flagValue);
                  if (!flagValue.isEmpty()) {
                    valueBuilder.append(flagValue.substring(1)).append(" ");
                  }
                } else {
                  flags.add(new FlagArgument(option, Strings.removeQuotations(flagValue)));
                }
              } else {
                toValue = option;
              }
            } else {
              flags.add(new FlagArgument(option, null));
            }
            arguments.remove(i - left);
            left++;
          }
        } else if (toValue != null) {
          String flagValue = null;
          if (building) {
            if (argument.endsWith("\"")) {
              building = false;
              valueBuilder.append(argument, 0, argument.length() - 1);
              flagValue = valueBuilder.toString();
              valueBuilder.setLength(0);
            } else {
              valueBuilder.append(argument).append(" ");
            }
          } else {
            if (build && Strings.isStart(argument)) {
              building = true;
              valueBuilder.append(argument.substring(1)).append(" ");
            } else {
              flagValue = Strings.removeQuotations(argument);
            }
          }
          if (flagValue != null) {
            flags.add(new FlagArgument(toValue, flagValue));
            toValue = null;
          }
          arguments.remove(i - left);
          left++;
        }
      }
      return this;
    }

    /**
     * Get the argument line as an array. The array is split using a blank-space (' ')
     *
     * @return the array
     */
    @NonNull
    public String[] getArgumentsArray() {
      return this.arguments.toArray(new String[0]);
    }

    @NonNull
    private Optional<? extends Option> getOption(@NonNull String alias) {
      return options.stream()
          .filter(options -> options.getAliases().contains(this.fixAlias(alias)))
          .findFirst();
    }

    private String fixAlias(@NonNull String name) {
      return name.contains("=") ? name.split("=")[0] : name;
    }

    private boolean isFlag(@NonNull String argument) {
      return argument.startsWith("--") || argument.startsWith("-");
    }

    /**
     * Get the argument line as a single string.
     *
     * @return the string
     */
    @NonNull
    public String getArgumentsString() {
      return Strings.join(arguments.toArray(new String[0]));
    }

    @Override
    public String toString() {
      return "Parser{"
          + "arguments="
          + arguments
          + ", options="
          + options
          + ", flags="
          + flags
          + ", valueBuilder="
          + valueBuilder
          + ", building="
          + building
          + ", toValue="
          + toValue
          + '}';
    }
  }
}

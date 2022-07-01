package me.googas.commands.flags;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a {@link Flag} which have been parsed to be used in a {@link
 * me.googas.commands.StarboxCommand}.
 */
public class Option implements StarboxFlag {

  @NonNull @Getter private final List<String> aliases;
  @NonNull @Getter private final String description;
  @Getter private final boolean valuable;
  private final String value;

  private Option(
      @NonNull List<String> aliases, @NonNull String description, boolean valuable, String value) {
    this.aliases = aliases;
    this.description = description;
    this.value = value;
    this.valuable = valuable;
  }

  /**
   * Create a new option/flag.
   *
   * @param description a short description of the flag
   * @param value the default value of the flag
   * @param valuable whether this flag requires a value
   * @param aliases the aliases of the flag
   * @return a new flag/option
   */
  @NonNull
  public static Option create(
      @NonNull String description, String value, boolean valuable, @NonNull String... aliases) {
    List<String> list = new ArrayList<>();
    if (aliases.length == 0) {
      throw new IllegalArgumentException("Array of aliases cannot be empty");
    }
    for (String alias : aliases) {
      if (alias.startsWith("-") || alias.startsWith("--")) {
        list.add(alias);
      } else {
        list.add(list.isEmpty() ? "--" + alias : "-" + alias);
      }
    }
    return new Option(list, description, valuable, value);
  }

  /**
   * Create a new option/flag.
   *
   * @param description a short description of the flag
   * @param valuable whether this flag requires a value
   * @param aliases the aliases of the flag
   * @return a new flag/option
   */
  public static Option create(
      @NonNull String description, boolean valuable, @NonNull String aliases) {
    return Option.create(description, null, valuable, aliases);
  }

  /**
   * Create a new option/flag.
   *
   * @param valuable whether this flag requires a value
   * @param aliases the aliases of the flag
   * @return a new flag/option
   */
  @NonNull
  public static Option create(boolean valuable, @NonNull String... aliases) {
    return Option.create("No description provided", "", valuable, aliases);
  }

  /**
   * Get the array of annotations parsed as a list.
   *
   * @param flags the array to parse
   * @return the list of options
   */
  @NonNull
  public static List<Option> of(@NonNull Flag[] flags) {
    List<Option> options = new ArrayList<>();
    for (Flag flag : flags) {
      options.add(Option.of(flag));
    }
    return options;
  }

  /**
   * Parse a single annotation.
   *
   * @param flag the annotation to parse
   * @return the parsed flag
   */
  @NonNull
  public static Option of(@NonNull Flag flag) {
    return Option.create(flag.description(), flag.value(), flag.valuable(), flag.aliases());
  }

  @Override
  public String toString() {
    return "Option{" + "aliases=" + aliases + ", valuable=" + valuable + '}';
  }

  @Override
  public @NonNull Optional<String> getValue() {
    return Optional.ofNullable(value);
  }
}

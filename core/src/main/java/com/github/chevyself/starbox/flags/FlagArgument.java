package com.github.chevyself.starbox.flags;

import java.util.Collection;
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
}

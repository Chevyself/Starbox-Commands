package com.starfishst.core.objects;

import java.util.Arrays;
import me.googas.commons.Strings;
import org.jetbrains.annotations.NotNull;

/**
 * Its a class that stores the strings joined from a command execution (from it's argument position)
 */
public class JoinedStrings {

  /** The split of strings from an executed command */
  @NotNull private final String[] strings;
  /** The single string of an executed command */
  @NotNull private final String string;

  /**
   * Create a new instance of {@link JoinedStrings}
   *
   * @param strings the array to start the instance
   */
  public JoinedStrings(@NotNull String[] strings) {
    this.strings = strings;
    this.string = Strings.fromArray(strings);
  }

  /**
   * Get the joined strings from a command execution as array
   *
   * @return the strings from the command execution as array
   */
  @NotNull
  public String[] getStrings() {
    return this.strings;
  }

  /**
   * Get the joined strings from a command execution
   *
   * @return the built strings
   */
  @NotNull
  public String getString() {
    return this.string;
  }

  @Override
  public String toString() {
    return "JoinedStrings{"
        + "strings="
        + Arrays.toString(this.strings)
        + ", string='"
        + this.string
        + '\''
        + '}';
  }
}

package com.starfishst.core.objects;

import lombok.Getter;
import lombok.NonNull;
import me.googas.commons.Strings;
import me.googas.commons.builder.ToStringBuilder;

/**
 * Its a class that stores the strings joined from a command execution (from it's argument position)
 */
public class JoinedStrings {

  /** The split of strings from an executed command */
  @NonNull @Getter private final String[] strings;
  /** The single string of an executed command */
  @NonNull @Getter private final String string;

  /**
   * Create a new instance of {@link JoinedStrings}
   *
   * @param strings the array to start the instance
   */
  public JoinedStrings(@NonNull String[] strings) {
    this.strings = strings;
    this.string = Strings.fromArray(strings);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("strings", (Object[]) strings)
        .append("string", string)
        .build();
  }
}

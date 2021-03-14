package me.googas.commands.objects;

import lombok.Getter;
import lombok.NonNull;
import me.googas.starbox.Strings;
import me.googas.starbox.builder.Builder;
import me.googas.starbox.builder.ToStringBuilder;

/**
 * Its a class that stores the strings joined from a command execution (from it's argument position)
 */
public class JoinedStrings implements Builder<String> {

  @NonNull @Getter private final String[] strings;
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
  public @NonNull String build() {
    return this.string;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("strings", (Object[]) strings)
        .append("string", string)
        .build();
  }
}

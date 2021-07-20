package me.googas.commands.objects;

import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.starbox.Strings;

/**
 * This class is a wrapped array of {@link String}. This is used for commands that require many
 * strings as an argument. You can check this instance being used in the context of a command
 * execution too in {@link StarboxCommandContext}
 *
 * @see me.googas.commands.arguments.MultipleArgument
 * @see StarboxCommandContext
 */
public class JoinedStrings {

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
  public String toString() {
    return this.string;
  }
}

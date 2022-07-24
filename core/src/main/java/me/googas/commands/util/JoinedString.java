package me.googas.commands.util;

import lombok.Getter;
import lombok.NonNull;

/**
 * This is a grouped string.
 *
 * @see Strings#group(Iterable)
 */
public final class JoinedString {

  @NonNull @Getter private final String string;
  @Getter private final int size;

  JoinedString(@NonNull String string, int size) {
    this.string = string;
    this.size = size;
  }
}

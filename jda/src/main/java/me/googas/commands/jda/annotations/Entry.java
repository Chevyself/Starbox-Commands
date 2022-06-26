package me.googas.commands.jda.annotations;

import lombok.NonNull;

/**
 * Represents the entry of a {@link java.util.Map}. This can be used to parse maps from annotations.
 */
public @interface Entry {

  /**
   * Get the key of the entry.
   *
   * @return the key
   */
  @NonNull
  String key();

  /**
   * Get the value of the entry.
   *
   * @return the value
   */
  @NonNull
  String value();
}

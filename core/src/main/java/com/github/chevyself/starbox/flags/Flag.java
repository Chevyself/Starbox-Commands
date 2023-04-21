package com.github.chevyself.starbox.flags;

import lombok.NonNull;

/** The state of an {@link Option} to be used inside command annotations. */
public @interface Flag {

  /**
   * Get the aliases of the flag. Do not use prefixes while naming, this means that you should not
   * use '-' or '--'. The first alias will be considered the 'name' of the flag which means that it
   * can be used with two dashes ('--') every other alias will only have one
   *
   * @return the array of aliases.
   */
  @NonNull
  String[] aliases();

  /**
   * Get the description of the flag. This may be just a short description
   *
   * @return the description
   */
  @NonNull
  String description() default "No description provided";

  /**
   * Get the default value of the flag.
   *
   * @return the default value as a {@link String}
   */
  @NonNull
  String value();

  /**
   * Get whether this flag requires a value.
   *
   * @return true if it requires a value
   */
  boolean valuable() default true;
}

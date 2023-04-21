package com.github.chevyself.starbox.flags;

import java.util.Collection;
import java.util.Optional;
import lombok.NonNull;

/** Interface which may be implemented by Flag classes. */
public interface StarboxFlag {

  /**
   * Get the aliases of the flag.
   *
   * @return the collection of aliases
   */
  @NonNull
  Collection<String> getAliases();

  /**
   * Get the description of the flag.
   *
   * @return the description which may be a short one
   */
  @NonNull
  String getDescription();

  /**
   * Check if the flag has an alias. It will check with both prefixes ('--') and ('-')
   *
   * @param alias the alias to check
   * @return true if this flag has the alias
   */
  default boolean hasAlias(@NonNull String alias) {
    boolean hasPrefix = alias.startsWith("--") || alias.startsWith("-");
    Collection<String> aliases = this.getAliases();
    return aliases.contains(alias)
        || (!hasPrefix && (aliases.contains("--" + alias) || alias.contains("-" + alias)));
  }

  /**
   * Get the value of the flag.
   *
   * @return the value
   */
  @NonNull
  Optional<String> getValue();
}

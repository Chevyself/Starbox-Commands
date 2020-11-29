package com.starfishst.core.context;

import java.util.List;
import lombok.NonNull;

/**
 * If a class can have a list of suggestions (mostly used for tab complete in case of Bukkit
 * commands)
 */
public interface ISuggestible {

  /**
   * Get a list of suggestions
   *
   * @param context the command context
   * @return the list of suggestions
   */
  @NonNull
  List<String> getSuggestions(@NonNull ICommandContext context);
}

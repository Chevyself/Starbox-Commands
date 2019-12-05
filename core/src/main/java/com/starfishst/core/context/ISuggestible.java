package com.starfishst.core.context;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/** If a class can have a list of suggestions (mostly used for tab complete) */
public interface ISuggestible {

  /**
   * Get a list of suggestions
   *
   * @param context the command context
   * @return the list of suggestions
   */
  @NotNull
  List<String> getSuggestions(@NotNull ICommandContext context);
}

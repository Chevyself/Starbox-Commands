package com.github.chevyself.starbox.common.tab;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.providers.type.StarboxArgumentProvider;
import java.util.List;
import lombok.NonNull;

public interface SuggestionsArgumentProvider<O, C extends StarboxCommandContext<C, ?>>
    extends StarboxArgumentProvider<O, C> {

  /**
   * Get the suggestions for the command.
   *
   * @param string the string that is in the argument position at the moment
   * @param context the context of the command
   * @return a list of suggestions of the command
   */
  @NonNull
  List<String> getSuggestions(@NonNull String string, @NonNull C context);
}

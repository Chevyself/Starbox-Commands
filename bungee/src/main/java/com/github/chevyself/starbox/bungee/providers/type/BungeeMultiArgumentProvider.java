package com.github.chevyself.starbox.bungee.providers.type;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.providers.type.StarboxMultipleArgumentProvider;
import java.util.List;
import lombok.NonNull;

/**
 * A bungee provider for multiple arguments.
 *
 * @param <O> the type of the object to provider
 */
public interface BungeeMultiArgumentProvider<O>
    extends StarboxMultipleArgumentProvider<O, CommandContext> {

  /**
   * Get the suggestions for the command.
   *
   * @param context the context of the command
   * @return a list of suggestions of the command
   */
  @NonNull
  List<String> getSuggestions(@NonNull CommandContext context);
}

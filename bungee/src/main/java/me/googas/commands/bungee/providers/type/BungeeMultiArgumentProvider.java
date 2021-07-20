package me.googas.commands.bungee.providers.type;

import java.util.List;
import lombok.NonNull;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.providers.type.StarboxMultipleArgumentProvider;

/**
 * A bungee provider for multiple arguments
 *
 * @param <O> the type of the object to provider
 */
public interface BungeeMultiArgumentProvider<O>
    extends StarboxMultipleArgumentProvider<O, CommandContext> {

  /**
   * Get the suggestions for the command
   *
   * @param context the context of the command
   * @return a list of suggestions of the command
   */
  @NonNull
  List<String> getSuggestions(@NonNull CommandContext context);
}

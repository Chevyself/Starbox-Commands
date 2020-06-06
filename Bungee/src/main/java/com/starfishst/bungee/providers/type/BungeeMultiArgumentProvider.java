package com.starfishst.bungee.providers.type;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.core.providers.type.IMultipleArgumentProvider;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * A bungee provider for multiple arguments
 *
 * @param <O> the type of the object to provider
 */
public interface BungeeMultiArgumentProvider<O>
    extends IMultipleArgumentProvider<O, CommandContext> {

  /**
   * Get the suggestions for the command
   *
   * @param context the context of the command
   * @return a list of suggestions of the command
   */
  @NotNull
  List<String> getSuggestions(@NotNull CommandContext context);
}

package com.starfishst.bungee.providers.type;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.core.providers.type.IArgumentProvider;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * A provider made for bungee commands
 *
 * @param <O> the type of the object to provider
 */
public interface BungeeArgumentProvider<O> extends IArgumentProvider<O, CommandContext> {

  /**
   * Get the suggestions for a command
   *
   * @param context the context of the command
   * @return the suggestions
   */
  @NotNull
  List<String> getSuggestions(CommandContext context);
}

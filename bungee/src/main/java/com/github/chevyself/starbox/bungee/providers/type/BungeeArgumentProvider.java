package com.github.chevyself.starbox.bungee.providers.type;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.providers.type.StarboxArgumentProvider;
import java.util.List;
import lombok.NonNull;

/**
 * A provider made for bungee commands.
 *
 * @param <O> the type of the object to provider
 */
public interface BungeeArgumentProvider<O> extends StarboxArgumentProvider<O, CommandContext> {

  /**
   * Get the suggestions for a command.
   *
   * @param context the context of the command
   * @return the suggestions
   */
  @NonNull
  List<String> getSuggestions(CommandContext context);
}

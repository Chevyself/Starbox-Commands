package com.starfishst.commands.bungee.providers.type;

import com.starfishst.commands.bungee.context.CommandContext;
import com.starfishst.core.providers.type.IArgumentProvider;
import java.util.List;
import lombok.NonNull;

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
  @NonNull
  List<String> getSuggestions(CommandContext context);
}

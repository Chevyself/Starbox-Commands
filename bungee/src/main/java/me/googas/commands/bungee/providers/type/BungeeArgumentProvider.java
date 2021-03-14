package me.googas.commands.bungee.providers.type;

import java.util.List;
import lombok.NonNull;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.providers.type.IArgumentProvider;

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

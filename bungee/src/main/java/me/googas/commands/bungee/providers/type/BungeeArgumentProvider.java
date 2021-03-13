package me.googas.commands.bungee.providers.type;

import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.providers.type.IArgumentProvider;
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

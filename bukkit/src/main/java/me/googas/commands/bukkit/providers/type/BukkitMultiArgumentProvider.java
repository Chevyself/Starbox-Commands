package me.googas.commands.bukkit.providers.type;

import java.util.List;
import lombok.NonNull;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.providers.type.StarboxMultipleArgumentProvider;

/** It's a provider made for bukkit commands. */
public interface BukkitMultiArgumentProvider<O>
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

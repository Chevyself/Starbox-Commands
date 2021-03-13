package me.googas.commands.bukkit.providers.type;

import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.providers.type.IArgumentProvider;
import java.util.List;
import lombok.NonNull;

/** It's a provider made for bukkit commands */
public interface BukkitArgumentProvider<O> extends IArgumentProvider<O, CommandContext> {

  /**
   * Get the suggestions for the command
   *
   * @param string the string that is in the argument position at the moment
   * @param context the context of the command
   * @return a list of suggestions of the command
   */
  @NonNull
  List<String> getSuggestions(@NonNull String string, CommandContext context);
}

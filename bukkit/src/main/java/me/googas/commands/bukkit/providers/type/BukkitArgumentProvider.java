package me.googas.commands.bukkit.providers.type;

import java.util.List;
import lombok.NonNull;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.providers.type.StarboxArgumentProvider;

/** An extension for providers made for bukkit commands. */
public interface BukkitArgumentProvider<O> extends StarboxArgumentProvider<O, CommandContext> {

  /**
   * Get the suggestions for the command.
   *
   * @param string the string that is in the argument position at the moment
   * @param context the context of the command
   * @return a list of suggestions of the command
   */
  @NonNull
  List<String> getSuggestions(@NonNull String string, CommandContext context);
}

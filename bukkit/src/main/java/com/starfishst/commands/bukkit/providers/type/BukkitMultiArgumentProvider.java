package com.starfishst.commands.bukkit.providers.type;

import com.starfishst.commands.bukkit.context.CommandContext;
import me.googas.commands.providers.type.IMultipleArgumentProvider;
import java.util.List;
import lombok.NonNull;

/** It's a provider made for bukkit commands */
public interface BukkitMultiArgumentProvider<O>
    extends IMultipleArgumentProvider<O, CommandContext> {

  /**
   * Get the suggestions for the command
   *
   * @param context the context of the command
   * @return a list of suggestions of the command
   */
  @NonNull
  List<String> getSuggestions(@NonNull CommandContext context);
}

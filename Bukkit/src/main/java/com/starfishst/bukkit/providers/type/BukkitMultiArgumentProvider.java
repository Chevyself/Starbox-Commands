package com.starfishst.bukkit.providers.type;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.core.providers.type.IMultipleArgumentProvider;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/** It's a provider made for bukkit commands */
public interface BukkitMultiArgumentProvider<O>
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

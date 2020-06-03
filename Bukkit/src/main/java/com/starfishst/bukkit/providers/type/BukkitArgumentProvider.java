package com.starfishst.bukkit.providers.type;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.core.providers.type.IArgumentProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/** It's a provider made for bukkit commands */
public interface BukkitArgumentProvider<O> extends IArgumentProvider<O, CommandContext> {

  /**
   * Get the suggestions for the command
   *
   * @param context the context of the command
   * @return a list of suggestions of the command
   */
  @NotNull
  List<String> getSuggestions(CommandContext context);
}

package com.starfishst.commands.providers;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.core.providers.type.IArgumentProvider;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/** It's a provider made for bukkit commands */
public interface BukkitArgumentProvider extends IArgumentProvider {

  /**
   * Get the suggestions for the command
   *
   * @param context the context of the command
   * @return a list of suggestions of the command
   */
  @NotNull
  List<String> getSuggestions(CommandContext context);
}

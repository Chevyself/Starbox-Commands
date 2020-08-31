package com.starfishst.bukkit.providers.targets;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.providers.type.BukkitArgumentProvider;
import com.starfishst.bukkit.targets.TargetNearestPlayer;
import com.starfishst.core.exceptions.ArgumentProviderException;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/** Provides the target selector of the nearest player */
public class TargetNearestPlayerProvider implements BukkitArgumentProvider<TargetNearestPlayer> {

  @Override
  public @NotNull Class<TargetNearestPlayer> getClazz() {
    return TargetNearestPlayer.class;
  }

  @NotNull
  @Override
  public TargetNearestPlayer fromString(@NotNull String string, @NotNull CommandContext context)
      throws ArgumentProviderException {
    return null;
  }

  @Override
  public @NotNull List<String> getSuggestions(@NotNull String string, CommandContext context) {
    return null;
  }
}

package com.starfishst.bukkit.providers;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.providers.type.BukkitArgumentProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Provides the command with an offline player. It queries the players that have played in the
 * server
 */
public class OfflinePlayerProvider implements BukkitArgumentProvider<OfflinePlayer> {

  @Override
  public @NotNull Class<OfflinePlayer> getClazz() {
    return OfflinePlayer.class;
  }

  @NotNull
  @Override
  public OfflinePlayer fromString(@NotNull String string, @NotNull CommandContext context)
      throws ArgumentProviderException {
    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
      if (player.getName() != null && player.getName().equalsIgnoreCase(string)) {
        return player;
      }
    }
    throw new ArgumentProviderException(
        context.getMessagesProvider().invalidPlayer(context.getString(), context));
  }

  @Override
  public @NotNull List<String> getSuggestions(@NotNull String string, CommandContext context) {
    List<String> suggestions = new ArrayList<>();
    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
      if (player.getName() != null) suggestions.add(player.getName());
    }
    return suggestions;
  }
}

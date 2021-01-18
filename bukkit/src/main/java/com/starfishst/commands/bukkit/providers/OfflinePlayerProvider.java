package com.starfishst.commands.bukkit.providers;

import com.starfishst.commands.bukkit.context.CommandContext;
import com.starfishst.commands.bukkit.providers.type.BukkitArgumentProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Provides the command with an offline player. It queries the players that have played in the
 * server
 */
public class OfflinePlayerProvider implements BukkitArgumentProvider<OfflinePlayer> {

  @Override
  public @NonNull Class<OfflinePlayer> getClazz() {
    return OfflinePlayer.class;
  }

  @NonNull
  @Override
  public OfflinePlayer fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
      if (player.getName() != null && player.getName().equalsIgnoreCase(string)) {
        return player;
      }
    }
    return context.get(string, Player.class, context);
  }

  @Override
  public @NonNull List<String> getSuggestions(@NonNull String string, CommandContext context) {
    List<String> suggestions = new ArrayList<>();
    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
      if (player.getName() != null) suggestions.add(player.getName());
    }
    return suggestions;
  }
}

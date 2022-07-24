package me.googas.commands.bukkit.providers;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.bukkit.CommandManager;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.providers.type.BukkitArgumentProvider;
import me.googas.commands.exceptions.ArgumentProviderException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/** Provides the {@link CommandManager} with the object {@link OfflinePlayer}. */
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
      if (player.getName() != null) {
        suggestions.add(player.getName());
      }
    }
    return suggestions;
  }
}

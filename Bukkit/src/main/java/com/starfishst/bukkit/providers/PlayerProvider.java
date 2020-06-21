package com.starfishst.bukkit.providers;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.providers.type.BukkitArgumentProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.bukkit.CommandManager} with the object of {@link Player} */
public class PlayerProvider implements BukkitArgumentProvider<Player> {

  /** The provider of the message in case the player is not found */
  @NotNull private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider the provider of the message in case the player is not found
   */
  public PlayerProvider(@NotNull MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  /**
   * Get the name of the players that are online the server
   *
   * @return the name of the players that are online the server
   */
  public static @NotNull List<String> getPlayerNames() {
    List<String> names = new ArrayList<>();
    Bukkit.getOnlinePlayers().forEach(player -> names.add(player.getName()));
    return names;
  }

  @Override
  public @NotNull List<String> getSuggestions(@NotNull String string, CommandContext context) {
    return getPlayerNames();
  }

  @Override
  public @NotNull Class<Player> getClazz() {
    return Player.class;
  }

  @NotNull
  @Override
  public Player fromString(@NotNull String string, @NotNull CommandContext context)
      throws ArgumentProviderException {
    Player player = Bukkit.getPlayer(string);
    if (player != null) {
      return player;
    } else {
      throw new ArgumentProviderException(messagesProvider.invalidPlayer(string, context));
    }
  }
}

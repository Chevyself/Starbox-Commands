package com.starfishst.commands.providers;

import com.starfishst.commands.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerProvider extends ArgumentProvider<Player> {

  public PlayerProvider() {
    super(Player.class);
  }

  @Override
  public @NotNull List<String> suggestions(@NotNull CommandSender sender) {
    List<String> playerNames = new ArrayList<>();
    Bukkit.getOnlinePlayers().forEach(player -> playerNames.add(player.getName()));
    return playerNames;
  }

  @NotNull
  @Override
  public Player fromString(@NotNull String string) throws ArgumentProviderException {
    Player player = Bukkit.getPlayer(string);
    if (player != null) {
      return player;
    } else {
      throw new ArgumentProviderException("&e{0} &cis not online");
    }
  }
}

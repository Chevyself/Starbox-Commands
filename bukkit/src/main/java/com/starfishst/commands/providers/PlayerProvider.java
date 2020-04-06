package com.starfishst.commands.providers;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerProvider implements BukkitArgumentProvider<Player> {

    @Override
    public @NotNull List<String> getSuggestions(CommandContext context) {
        List<String> names = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> names.add(player.getName()));
        return names;
    }

    @Override
    public @NotNull Class<Player> getClazz() {
        return Player.class;
    }

    @NotNull
    @Override
    public Player fromString(@NotNull String string, @NotNull ICommandContext<?> context)
            throws ArgumentProviderException {
        Player player = Bukkit.getPlayer(string);
        if (player != null) {
            return player;
        } else {
            throw new ArgumentProviderException("&e{0} &cis not online");
        }
    }
}

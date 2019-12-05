package com.starfishst.commands.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerSenderProvider implements IExtraArgumentProvider<Player> {

  @NotNull
  @Override
  public Player getObject(@NotNull ICommandContext context) throws ArgumentProviderException {
    if (context.getSender() instanceof Player) {
      return (Player) context.getSender();
    } else {
      throw new ArgumentProviderException("&cOnly players can use this command");
    }
  }

  @Override
  public @NotNull Class<?> getClazz() {
    return Player.class;
  }
}

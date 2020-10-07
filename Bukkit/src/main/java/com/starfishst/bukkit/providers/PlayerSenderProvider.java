package com.starfishst.bukkit.providers;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.providers.type.BukkitExtraArgumentProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Provides the {@link com.starfishst.bukkit.CommandManager} with the object of {@link Player} when
 * is a sender
 */
public class PlayerSenderProvider implements BukkitExtraArgumentProvider<Player> {

  /**
   * The provider of the message in case the command is executed by other entity rather than a
   * player
   */
  @NotNull private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider the provider of the message in case the command is executed by
   *     something rather than a player
   */
  public PlayerSenderProvider(@NotNull MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NotNull Class<Player> getClazz() {
    return Player.class;
  }

  @NotNull
  @Override
  public Player getObject(@NotNull CommandContext context) throws ArgumentProviderException {
    if (context.getSender() instanceof Player) {
      return (Player) context.getSender();
    } else {
      throw new ArgumentProviderException(messagesProvider.playersOnly(context));
    }
  }
}

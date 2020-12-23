package com.starfishst.bukkit.providers;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.providers.type.BukkitExtraArgumentProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import lombok.NonNull;
import org.bukkit.entity.Player;

/**
 * Provides the {@link com.starfishst.bukkit.CommandManager} with the object of {@link Player} when
 * is a sender
 */
public class PlayerSenderProvider implements BukkitExtraArgumentProvider<Player> {

  @NonNull private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider the provider of the message in case the command is executed by
   *     something rather than a player
   */
  public PlayerSenderProvider(@NonNull MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Class<Player> getClazz() {
    return Player.class;
  }

  @NonNull
  @Override
  public Player getObject(@NonNull CommandContext context) throws ArgumentProviderException {
    if (context.getSender() instanceof Player) {
      return (Player) context.getSender();
    } else {
      throw new ArgumentProviderException(messagesProvider.playersOnly(context));
    }
  }
}

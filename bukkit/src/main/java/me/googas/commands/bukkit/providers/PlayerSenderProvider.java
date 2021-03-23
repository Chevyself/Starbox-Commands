package me.googas.commands.bukkit.providers;

import lombok.NonNull;
import me.googas.commands.bukkit.CommandManager;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.messages.MessagesProvider;
import me.googas.commands.bukkit.providers.type.BukkitExtraArgumentProvider;
import me.googas.commands.exceptions.ArgumentProviderException;
import org.bukkit.entity.Player;

/** Provides the {@link CommandManager} with the object of {@link Player} when is a sender */
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

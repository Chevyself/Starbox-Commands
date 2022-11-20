package chevyself.github.commands.bukkit.providers;

import chevyself.github.commands.bukkit.CommandManager;
import chevyself.github.commands.bukkit.context.CommandContext;
import chevyself.github.commands.bukkit.messages.MessagesProvider;
import chevyself.github.commands.bukkit.providers.type.BukkitArgumentProvider;
import chevyself.github.commands.bukkit.providers.type.BukkitExtraArgumentProvider;
import chevyself.github.commands.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/** Provides the {@link CommandManager} with the object {@link Player}. */
public class PlayerProvider
    implements BukkitArgumentProvider<Player>, BukkitExtraArgumentProvider<Player> {

  @NonNull private final MessagesProvider messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider the provider of the message in case the player is not found
   */
  public PlayerProvider(@NonNull MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  /**
   * Get the name of the players that are online the server.
   *
   * @return the name of the players that are online the server
   */
  public static @NonNull List<String> getPlayerNames() {
    List<String> names = new ArrayList<>();
    Bukkit.getOnlinePlayers().forEach(player -> names.add(player.getName()));
    return names;
  }

  @Override
  public @NonNull List<String> getSuggestions(@NonNull String string, CommandContext context) {
    return PlayerProvider.getPlayerNames();
  }

  @Override
  public @NonNull Class<Player> getClazz() {
    return Player.class;
  }

  @NonNull
  @Override
  public Player fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    Player player = Bukkit.getPlayer(string);
    if (player != null) {
      return player;
    } else {
      throw new ArgumentProviderException(this.messagesProvider.invalidPlayer(string, context));
    }
  }

  @Override
  public @NonNull Player getObject(@NonNull CommandContext context)
      throws ArgumentProviderException {
    if (context.getSender() instanceof Player) {
      return (Player) context.getSender();
    } else {
      throw new ArgumentProviderException(this.messagesProvider.playersOnly(context));
    }
  }
}

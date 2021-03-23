package me.googas.commands.bungee.providers;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.messages.MessagesProvider;
import me.googas.commands.bungee.providers.type.BungeeArgumentProvider;
import me.googas.commands.exceptions.ArgumentProviderException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/** Provides a proxied player */
public class ProxiedPlayerProvider implements BungeeArgumentProvider<ProxiedPlayer> {

  @NonNull private final MessagesProvider messagesProvider;

  /**
   * Create the provider
   *
   * @param messagesProvider the message provider for the error
   */
  public ProxiedPlayerProvider(@NonNull MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull List<String> getSuggestions(CommandContext context) {
    List<String> names = new ArrayList<>();
    for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
      names.add(player.getName());
    }
    return names;
  }

  @Override
  public @NonNull Class<ProxiedPlayer> getClazz() {
    return ProxiedPlayer.class;
  }

  @NonNull
  @Override
  public ProxiedPlayer fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(string);
    if (player != null) return player;
    throw new ArgumentProviderException(messagesProvider.invalidPlayer(string, context));
  }
}

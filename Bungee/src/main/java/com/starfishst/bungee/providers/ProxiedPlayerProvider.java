package com.starfishst.bungee.providers;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.bungee.messages.MessagesProvider;
import com.starfishst.bungee.providers.type.BungeeArgumentProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

/** Provides a proxied player */
public class ProxiedPlayerProvider implements BungeeArgumentProvider<ProxiedPlayer> {

  /** The message provider for the error */
  @NotNull private final MessagesProvider messagesProvider;

  /**
   * Create the provider
   *
   * @param messagesProvider the message provider for the error
   */
  public ProxiedPlayerProvider(@NotNull MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  /**
   * Get the suggestions for a command
   *
   * @param context the context of the command
   * @return the suggestions
   */
  @Override
  public @NotNull List<String> getSuggestions(CommandContext context) {
    List<String> names = new ArrayList<>();
    for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
      names.add(player.getName());
    }
    return names;
  }

  @Override
  public @NotNull Class<ProxiedPlayer> getClazz() {
    return ProxiedPlayer.class;
  }

  @NotNull
  @Override
  public ProxiedPlayer fromString(@NotNull String string, @NotNull CommandContext context)
      throws ArgumentProviderException {
    return ProxyServer.getInstance().getPlayers().stream()
        .filter(player -> player.getName().equalsIgnoreCase(string))
        .findFirst()
        .orElseThrow(
            () -> new ArgumentProviderException(messagesProvider.invalidPlayer(string, context)));
  }
}

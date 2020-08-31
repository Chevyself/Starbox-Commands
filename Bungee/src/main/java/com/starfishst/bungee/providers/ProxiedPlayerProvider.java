package com.starfishst.bungee.providers;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.bungee.messages.MessagesProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

/** Provides a proxied player */
public class ProxiedPlayerProvider implements IArgumentProvider<ProxiedPlayer, CommandContext> {

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

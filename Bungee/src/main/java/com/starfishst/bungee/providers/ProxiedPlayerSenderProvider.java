package com.starfishst.bungee.providers;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.bungee.messages.MessagesProvider;
import com.starfishst.core.arguments.ExtraArgument;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

/** Provides with the player that executed the command */
public class ProxiedPlayerSenderProvider
    implements IExtraArgumentProvider<ProxiedPlayer, CommandContext> {

  /** The provider for the error message */
  @NotNull private final MessagesProvider messagesProvider;

  /**
   * Create the provider
   *
   * @param messagesProvider the provider for the error message
   */
  public ProxiedPlayerSenderProvider(@NotNull MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  /**
   * Get the class to provide
   *
   * @return the class to provide
   */
  @Override
  public @NotNull Class<ProxiedPlayer> getClazz() {
    return ProxiedPlayer.class;
  }

  /**
   * Get the object using the command context
   *
   * @param context the command context
   * @return the {@link ExtraArgument} object
   * @throws ArgumentProviderException in case that the object could not be given
   */
  @NotNull
  @Override
  public ProxiedPlayer getObject(@NotNull CommandContext context) throws ArgumentProviderException {
    if (context.getSender() instanceof ProxiedPlayer) {
      return (ProxiedPlayer) context.getSender();
    } else {
      throw new ArgumentProviderException(messagesProvider.onlyPlayers(context));
    }
  }
}

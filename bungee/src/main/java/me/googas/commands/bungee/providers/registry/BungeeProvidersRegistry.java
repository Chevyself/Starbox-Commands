package me.googas.commands.bungee.providers.registry;

import lombok.NonNull;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.messages.MessagesProvider;
import me.googas.commands.bungee.providers.CommandContextProvider;
import me.googas.commands.bungee.providers.CommandSenderProvider;
import me.googas.commands.bungee.providers.ProxiedPlayerProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;

/** The implementation of providers registry for bungee. */
public class BungeeProvidersRegistry extends ProvidersRegistry<CommandContext> {

  /**
   * Create the registry with the default providers.
   *
   * @param messages the messages providers for the messages sent in the provider
   */
  public BungeeProvidersRegistry(@NonNull MessagesProvider messages) {
    super(messages);
    this.addProvider(new CommandContextProvider())
        .addProvider(new CommandSenderProvider())
        .addProvider(new ProxiedPlayerProvider(messages));
  }
}

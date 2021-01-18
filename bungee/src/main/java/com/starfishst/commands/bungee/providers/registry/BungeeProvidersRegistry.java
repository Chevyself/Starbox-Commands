package com.starfishst.commands.bungee.providers.registry;

import com.starfishst.commands.bungee.context.CommandContext;
import com.starfishst.commands.bungee.messages.MessagesProvider;
import com.starfishst.commands.bungee.providers.CommandContextProvider;
import com.starfishst.commands.bungee.providers.CommandSenderProvider;
import com.starfishst.commands.bungee.providers.ProxiedPlayerProvider;
import com.starfishst.commands.bungee.providers.ProxiedPlayerSenderProvider;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import lombok.NonNull;

/** The implementation of providers registry for bungee */
public class BungeeProvidersRegistry extends ProvidersRegistry<CommandContext> {

  /**
   * Create the registry with the default providers
   *
   * @param messages the messages providers for the messages sent in the provider
   */
  public BungeeProvidersRegistry(@NonNull MessagesProvider messages) {
    super(messages);
    this.addProvider(new CommandContextProvider());
    this.addProvider(new CommandSenderProvider());
    this.addProvider(new ProxiedPlayerProvider(messages));
    this.addProvider(new ProxiedPlayerSenderProvider(messages));
  }
}

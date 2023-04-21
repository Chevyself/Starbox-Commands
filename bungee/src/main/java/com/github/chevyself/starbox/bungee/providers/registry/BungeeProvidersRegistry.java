package com.github.chevyself.starbox.bungee.providers.registry;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.messages.MessagesProvider;
import com.github.chevyself.starbox.bungee.providers.CommandContextProvider;
import com.github.chevyself.starbox.bungee.providers.CommandSenderProvider;
import com.github.chevyself.starbox.bungee.providers.ProxiedPlayerProvider;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import lombok.NonNull;

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

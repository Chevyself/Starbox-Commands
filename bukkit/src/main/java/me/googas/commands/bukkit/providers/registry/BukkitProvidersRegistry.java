package me.googas.commands.bukkit.providers.registry;

import lombok.NonNull;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.messages.MessagesProvider;
import me.googas.commands.bukkit.providers.CommandContextProvider;
import me.googas.commands.bukkit.providers.CommandSenderArgumentProvider;
import me.googas.commands.bukkit.providers.MaterialProvider;
import me.googas.commands.bukkit.providers.OfflinePlayerProvider;
import me.googas.commands.bukkit.providers.PlayerProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;

/** The providers registry for bukkit */
public class BukkitProvidersRegistry extends ProvidersRegistry<CommandContext> {

  /**
   * Create the registry with the default providers
   *
   * @param messages the messages providers for the messages sent in the provider
   */
  public BukkitProvidersRegistry(@NonNull MessagesProvider messages) {
    super(messages);
    this.addProvider(new CommandContextProvider());
    this.addProvider(new CommandSenderArgumentProvider());
    this.addProvider(new MaterialProvider(messages));
    this.addProvider(new OfflinePlayerProvider());
    this.addProvider(new PlayerProvider(messages));
  }

  /** Create the registry with the default providers */
  public BukkitProvidersRegistry() {}
}

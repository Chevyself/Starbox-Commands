package com.starfishst.bukkit.providers.registry;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.providers.CommandContextProvider;
import com.starfishst.bukkit.providers.CommandSenderArgumentProvider;
import com.starfishst.bukkit.providers.MaterialProvider;
import com.starfishst.bukkit.providers.OfflinePlayerProvider;
import com.starfishst.bukkit.providers.PlayerProvider;
import com.starfishst.bukkit.providers.PlayerSenderProvider;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import org.jetbrains.annotations.NotNull;

/** The providers registry for bukkit */
public class BukkitProvidersRegistry extends ProvidersRegistry<CommandContext> {

  /**
   * Create the registry with the default providers
   *
   * @param messages the messages providers for the messages sent in the provider
   */
  public BukkitProvidersRegistry(@NotNull MessagesProvider messages) {
    super(messages);
    this.addProvider(new CommandContextProvider());
    this.addProvider(new CommandSenderArgumentProvider());
    this.addProvider(new MaterialProvider(messages));
    this.addProvider(new OfflinePlayerProvider());
    this.addProvider(new PlayerProvider(messages));
    this.addProvider(new PlayerSenderProvider(messages));
  }

  /** Create the registry with the default providers */
  public BukkitProvidersRegistry() {}
}

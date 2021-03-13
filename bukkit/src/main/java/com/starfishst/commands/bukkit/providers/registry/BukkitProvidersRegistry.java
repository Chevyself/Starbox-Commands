package com.starfishst.commands.bukkit.providers.registry;

import com.starfishst.commands.bukkit.context.CommandContext;
import com.starfishst.commands.bukkit.messages.MessagesProvider;
import com.starfishst.commands.bukkit.providers.CommandContextProvider;
import com.starfishst.commands.bukkit.providers.CommandSenderArgumentProvider;
import com.starfishst.commands.bukkit.providers.MaterialProvider;
import com.starfishst.commands.bukkit.providers.OfflinePlayerProvider;
import com.starfishst.commands.bukkit.providers.PlayerProvider;
import com.starfishst.commands.bukkit.providers.PlayerSenderProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import lombok.NonNull;

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
    this.addProvider(new PlayerSenderProvider(messages));
  }

  /** Create the registry with the default providers */
  public BukkitProvidersRegistry() {}
}

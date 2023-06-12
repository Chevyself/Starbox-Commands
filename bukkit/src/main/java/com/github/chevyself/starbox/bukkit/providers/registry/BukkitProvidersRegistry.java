package com.github.chevyself.starbox.bukkit.providers.registry;

import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.messages.BukkitMessagesProvider;
import com.github.chevyself.starbox.bukkit.providers.CommandContextProvider;
import com.github.chevyself.starbox.bukkit.providers.CommandSenderArgumentProvider;
import com.github.chevyself.starbox.bukkit.providers.MaterialProvider;
import com.github.chevyself.starbox.bukkit.providers.OfflinePlayerProvider;
import com.github.chevyself.starbox.bukkit.providers.PlayerProvider;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import lombok.NonNull;

/** The providers' registry for bukkit. */
public class BukkitProvidersRegistry extends ProvidersRegistry<CommandContext> {

  /**
   * Create the registry with the default providers.
   *
   * @param messages the messages providers for the messages sent in the provider
   */
  public BukkitProvidersRegistry(@NonNull BukkitMessagesProvider messages) {
    super(messages);
    this.addProvider(new CommandContextProvider())
        .addProvider(new CommandSenderArgumentProvider())
        .addProvider(new MaterialProvider(messages))
        .addProvider(new OfflinePlayerProvider())
        .addProvider(new PlayerProvider(messages));
  }

  /** Create the registry with the default providers. */
  public BukkitProvidersRegistry() {}
}

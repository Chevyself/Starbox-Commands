package chevyself.github.commands.bukkit.providers.registry;

import chevyself.github.commands.bukkit.context.CommandContext;
import chevyself.github.commands.bukkit.messages.MessagesProvider;
import chevyself.github.commands.bukkit.providers.CommandContextProvider;
import chevyself.github.commands.bukkit.providers.CommandSenderArgumentProvider;
import chevyself.github.commands.bukkit.providers.MaterialProvider;
import chevyself.github.commands.bukkit.providers.OfflinePlayerProvider;
import chevyself.github.commands.bukkit.providers.PlayerProvider;
import chevyself.github.commands.providers.registry.ProvidersRegistry;
import lombok.NonNull;

/** The providers' registry for bukkit. */
public class BukkitProvidersRegistry extends ProvidersRegistry<CommandContext> {

  /**
   * Create the registry with the default providers.
   *
   * @param messages the messages providers for the messages sent in the provider
   */
  public BukkitProvidersRegistry(@NonNull MessagesProvider messages) {
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

package chevyself.github.commands.bungee.providers.registry;

import chevyself.github.commands.bungee.context.CommandContext;
import chevyself.github.commands.bungee.messages.MessagesProvider;
import chevyself.github.commands.bungee.providers.CommandContextProvider;
import chevyself.github.commands.bungee.providers.CommandSenderProvider;
import chevyself.github.commands.bungee.providers.ProxiedPlayerProvider;
import chevyself.github.commands.providers.registry.ProvidersRegistry;
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

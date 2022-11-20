package chevyself.github.commands.bungee.providers;

import chevyself.github.commands.bungee.context.CommandContext;
import chevyself.github.commands.bungee.providers.type.BungeeExtraArgumentProvider;
import lombok.NonNull;

/** Provides the command context to commands. */
public class CommandContextProvider implements BungeeExtraArgumentProvider<CommandContext> {

  @Override
  public @NonNull Class<CommandContext> getClazz() {
    return CommandContext.class;
  }

  @NonNull
  @Override
  public CommandContext getObject(@NonNull CommandContext context) {
    return context;
  }
}

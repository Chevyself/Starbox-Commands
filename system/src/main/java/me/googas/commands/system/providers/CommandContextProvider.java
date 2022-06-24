package me.googas.commands.system.providers;

import lombok.NonNull;
import me.googas.commands.providers.type.StarboxExtraArgumentProvider;
import me.googas.commands.system.context.CommandContext;

/**
 * Provides the {@link me.googas.commands.system.CommandManager} with the object of {@link
 * CommandContext}.
 */
public class CommandContextProvider
    implements StarboxExtraArgumentProvider<CommandContext, CommandContext> {

  @Override
  public @NonNull Class<CommandContext> getClazz() {
    return CommandContext.class;
  }

  @Override
  public @NonNull CommandContext getObject(@NonNull CommandContext context) {
    return context;
  }
}

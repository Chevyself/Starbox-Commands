package me.googas.commands.bungee.providers;

import lombok.NonNull;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.providers.type.BungeeExtraArgumentProvider;

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

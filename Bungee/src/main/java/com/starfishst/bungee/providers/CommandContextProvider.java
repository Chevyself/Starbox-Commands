package com.starfishst.bungee.providers;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.bungee.providers.type.BungeeExtraArgumentProvider;
import lombok.NonNull;

/** Provides the command context to commands */
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

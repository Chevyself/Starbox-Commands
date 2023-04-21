package com.github.chevyself.starbox.bungee.providers;

import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.providers.type.BungeeExtraArgumentProvider;
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

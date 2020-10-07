package com.starfishst.bungee.providers;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.bungee.providers.type.BungeeExtraArgumentProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import org.jetbrains.annotations.NotNull;

/** Provides the command context to commands */
public class CommandContextProvider implements BungeeExtraArgumentProvider<CommandContext> {
  @Override
  public @NotNull Class<CommandContext> getClazz() {
    return CommandContext.class;
  }

  @NotNull
  @Override
  public CommandContext getObject(@NotNull CommandContext context)
      throws ArgumentProviderException {
    return context;
  }
}

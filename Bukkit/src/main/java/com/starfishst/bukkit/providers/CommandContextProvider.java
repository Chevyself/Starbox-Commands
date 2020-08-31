package com.starfishst.bukkit.providers;

import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Provides the {@link com.starfishst.bukkit.CommandManager} with the object of {@link
 * CommandContext}
 */
public class CommandContextProvider
    implements IExtraArgumentProvider<CommandContext, CommandContext> {

  @Override
  public @NotNull Class<CommandContext> getClazz() {
    return CommandContext.class;
  }

  @NotNull
  @Override
  public CommandContext getObject(@NotNull CommandContext context) {
    return context;
  }
}

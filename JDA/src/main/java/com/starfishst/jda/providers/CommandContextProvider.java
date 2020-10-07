package com.starfishst.jda.providers;

import com.starfishst.core.providers.type.IExtraArgumentProvider;
import com.starfishst.jda.context.CommandContext;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link CommandContext} */
public class CommandContextProvider
    implements IExtraArgumentProvider<CommandContext, CommandContext> {

  @NotNull
  @Override
  public CommandContext getObject(@NotNull CommandContext context) {
    return context;
  }

  @Override
  public @NotNull Class<CommandContext> getClazz() {
    return CommandContext.class;
  }
}

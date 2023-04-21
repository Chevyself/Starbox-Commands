package com.github.chevyself.starbox.system.providers;

import com.github.chevyself.starbox.providers.type.StarboxExtraArgumentProvider;
import com.github.chevyself.starbox.system.CommandManager;
import com.github.chevyself.starbox.system.context.CommandContext;
import lombok.NonNull;

/** Provides the {@link CommandManager} with the object of {@link CommandContext}. */
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

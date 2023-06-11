package com.github.chevyself.starbox.system.providers;

import com.github.chevyself.starbox.providers.CommandContextProvider;
import com.github.chevyself.starbox.system.context.CommandContext;
import lombok.NonNull;

public class SystemCommandContextProvider extends CommandContextProvider<CommandContext> {

  @Override
  public @NonNull Class<CommandContext> getClazz() {
    return CommandContext.class;
  }
}

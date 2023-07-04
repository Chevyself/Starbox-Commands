package com.github.chevyself.starbox.velocity.providers;

import com.github.chevyself.starbox.providers.type.CommandContextProvider;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import lombok.NonNull;

public class VelocityCommandContextProvider extends CommandContextProvider<CommandContext> {
  @Override
  public @NonNull Class<CommandContext> getClazz() {
    return CommandContext.class;
  }
}

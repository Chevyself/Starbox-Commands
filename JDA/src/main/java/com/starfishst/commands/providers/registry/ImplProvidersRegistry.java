package com.starfishst.commands.providers.registry;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import org.jetbrains.annotations.NotNull;

public class ImplProvidersRegistry extends ProvidersRegistry<CommandContext> {

  /** The instance for static usage */
  @NotNull private static final ImplProvidersRegistry instance = new ImplProvidersRegistry();

  /**
   * Get the instance of the providers registry
   *
   * @return the instance of providers registry
   */
  @NotNull
  public static ImplProvidersRegistry getInstance() {
    return instance;
  }
}

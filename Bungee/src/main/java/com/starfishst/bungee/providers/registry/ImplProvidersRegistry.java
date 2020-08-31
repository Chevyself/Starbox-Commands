package com.starfishst.bungee.providers.registry;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import org.jetbrains.annotations.NotNull;

/** The registry for bungee commands */
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

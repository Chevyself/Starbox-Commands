package com.github.chevyself.starbox.adapters;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.GenericMessagesProvider;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import java.util.Objects;
import lombok.NonNull;

public abstract class AbstractAdapter<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>>
    implements Adapter<C, T> {

  @NonNull
  protected MessagesProvider<C> messagesProvider;
  @NonNull
  protected ProvidersRegistry<C> providersRegistry;
  @NonNull
  protected MiddlewareRegistry<C> middlewareRegistry;
  protected CommandManager<C, T> commandManager;
  private boolean useDefaultMiddlewares;
  private boolean useDefaultProviders;

  public AbstractAdapter() {
    this.messagesProvider = new GenericMessagesProvider<>();
    this.providersRegistry = new ProvidersRegistry<>();
    this.middlewareRegistry = new MiddlewareRegistry<>();
    this.useDefaultMiddlewares = true;
    this.useDefaultProviders = true;
  }

  private void checkNotInitialized() {
    if (commandManager != null) {
      throw new IllegalStateException("CommandManager has already been initialized.");
    }
  }

  @Override
  public @NonNull Adapter<C, T> setProvidersRegistry(
      @NonNull ProvidersRegistry<C> providersRegistry) {
    this.checkNotInitialized();
    this.providersRegistry = providersRegistry;
    return this;
  }

  @Override
  public @NonNull Adapter<C, T> setMiddlewareRegistry(
      @NonNull MiddlewareRegistry<C> middlewareRegistry) {
    this.checkNotInitialized();
    this.middlewareRegistry = middlewareRegistry;
    return this;
  }

  @Override
  public @NonNull Adapter<C, T> setMessagesProvider(@NonNull MessagesProvider<C> messagesProvider) {
    this.checkNotInitialized();
    this.messagesProvider = messagesProvider;
    return this;
  }

  @Override
  public @NonNull Adapter<C, T> useDefaultMiddlewares(boolean use) {
    this.checkNotInitialized();
    this.useDefaultMiddlewares = use;
    return this;
  }

  @Override
  public @NonNull Adapter<C, T> useDefaultProviders(boolean use) {
    this.checkNotInitialized();
    this.useDefaultProviders = use;
    return this;
  }

  @Override
  public @NonNull CommandManager<C, T> getCommandManager() {
    return Objects.requireNonNull(commandManager, "CommandManager has not been initialized yet.");
  }

  @Override
  public @NonNull CommandManager<C, T> initialize() {
    this.checkNotInitialized();
    if (useDefaultMiddlewares) {
      this.registerDefaultMiddlewares();
    }
    if (useDefaultProviders) {
      this.registerDefaultProviders();
    }
    this.commandManager = this.createCommandManager();
    return this.commandManager;
  }

  @NonNull
  protected abstract CommandManager<C, T> createCommandManager();

  protected void registerDefaultProviders() {
    this.providersRegistry.registerDefaults(this.messagesProvider);
  }

  protected void registerDefaultMiddlewares() {

  }
}

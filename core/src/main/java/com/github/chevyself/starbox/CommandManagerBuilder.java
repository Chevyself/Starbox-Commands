package com.github.chevyself.starbox;

import com.github.chevyself.starbox.adapters.Adapter;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.GenericMessagesProvider;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandMetadataParser;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import lombok.Getter;
import lombok.NonNull;

public class CommandManagerBuilder<
    C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  @NonNull private final Adapter<C, T> adapter;
  @NonNull @Getter private MessagesProvider<C> messagesProvider;
  @NonNull @Getter private ProvidersRegistry<C> providersRegistry;
  @NonNull @Getter private MiddlewareRegistry<C> middlewareRegistry;
  @Getter private boolean useDefaultMiddlewares;
  @Getter private boolean useDefaultProviders;
  @Getter private CommandMetadataParser commandMetadataParser;
  private CommandManager<C, T> built;

  public CommandManagerBuilder(@NonNull Adapter<C, T> adapter) {
    this.adapter = adapter;
    this.messagesProvider = new GenericMessagesProvider<>();
    this.providersRegistry = new ProvidersRegistry<>();
    this.middlewareRegistry = new MiddlewareRegistry<>();
    this.useDefaultMiddlewares = true;
    this.useDefaultProviders = true;
    this.commandMetadataParser = null;
  }

  public @NonNull CommandManagerBuilder<C, T> setProvidersRegistry(
      @NonNull ProvidersRegistry<C> providersRegistry) {
    this.checkNotInitialized();
    this.providersRegistry = providersRegistry;
    return this;
  }

  public @NonNull CommandManagerBuilder<C, T> setMiddlewareRegistry(
      @NonNull MiddlewareRegistry<C> middlewareRegistry) {
    this.checkNotInitialized();
    this.middlewareRegistry = middlewareRegistry;
    return this;
  }

  public @NonNull CommandManagerBuilder<C, T> setMessagesProvider(
      @NonNull MessagesProvider<C> messagesProvider) {
    this.checkNotInitialized();
    this.messagesProvider = messagesProvider;
    return this;
  }

  public CommandManagerBuilder<C, T> setCommandMetadataParser(
      CommandMetadataParser commandMetadataParser) {
    this.commandMetadataParser = commandMetadataParser;
    return this;
  }

  public @NonNull CommandManagerBuilder<C, T> useDefaultMiddlewares(boolean use) {
    this.checkNotInitialized();
    this.useDefaultMiddlewares = use;
    return this;
  }

  public @NonNull CommandManagerBuilder<C, T> useDefaultProviders(boolean use) {
    this.checkNotInitialized();
    this.useDefaultProviders = use;
    return this;
  }

  private void checkNotInitialized() {
    if (this.built != null) {
      throw new IllegalStateException("CommandManager has already been initialized.");
    }
  }

  public CommandManager<C, T> build() {
    if (built == null) {
      if (useDefaultMiddlewares) {
        this.adapter.registerDefaultMiddlewares(this, this.middlewareRegistry);
      }
      if (useDefaultProviders) {
        this.providersRegistry.registerDefaults(this.messagesProvider);
        this.adapter.registerDefaultProviders(this, this.providersRegistry);
      }
      CommandMetadataParser commandMetadataParser =
          this.commandMetadataParser == null
              ? this.adapter.getDefaultCommandMetadataParser()
              : this.commandMetadataParser;
      this.built =
          new CommandManager<>(
              this.adapter,
              this.providersRegistry,
              this.middlewareRegistry,
              this.messagesProvider,
              commandMetadataParser);
      this.adapter.onBuilt(this.built);
    }
    return this.built;
  }
}

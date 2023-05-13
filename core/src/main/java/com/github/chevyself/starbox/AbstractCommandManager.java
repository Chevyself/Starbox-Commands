package com.github.chevyself.starbox;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.StarboxMessagesProvider;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

public abstract class AbstractCommandManager<
        C extends StarboxCommandContext, T extends StarboxCommand<C, T>>
    implements StarboxCommandManager<C, T> {

  @NonNull @Getter private final CommandParser<?, C, T> parser;
  @NonNull @Getter private final ProvidersRegistry<C> providersRegistry;
  @NonNull @Getter private final StarboxMessagesProvider<C> messagesProvider;
  @NonNull @Getter private final List<Middleware<C>> globalMiddlewares;
  @NonNull @Getter private final List<Middleware<C>> middlewares;
  @NonNull @Getter private final List<T> commands;

  public AbstractCommandManager(
      @NonNull CommandParser<?, C, T> parser,
      @NonNull ProvidersRegistry<C> providersRegistry,
      @NonNull StarboxMessagesProvider<C> messagesProvider) {
    this.parser = parser;
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
    this.globalMiddlewares = new ArrayList<>();
    this.middlewares = new ArrayList<>();
    this.commands = new ArrayList<>();
  }

  @Override
  public @NonNull StarboxCommandManager<C, T> register(@NonNull T command) {
    commands.add(command);
    return this;
  }

  @Override
  public @NonNull StarboxCommandManager<C, T> addGlobalMiddleware(
      @NonNull Middleware<C> middleware) {
    this.globalMiddlewares.add(middleware);
    return this;
  }

  @Override
  public @NonNull StarboxCommandManager<C, T> addMiddleware(@NonNull Middleware<C> middleware) {
    this.middlewares.add(middleware);
    return this;
  }
}

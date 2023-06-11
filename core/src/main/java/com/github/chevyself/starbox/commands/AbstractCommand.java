package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

public abstract class AbstractCommand<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>>
    implements StarboxCommand<C, T> {

  @NonNull @Getter protected final List<String> aliases;
  @NonNull @Getter protected final List<Middleware<C>> middlewares;
  @NonNull @Getter protected final List<Option> options;
  @NonNull @Getter protected final List<T> children;
  @NonNull @Getter protected final ProvidersRegistry<C> providersRegistry;
  @NonNull @Getter protected final MessagesProvider<C> messagesProvider;
  @NonNull @Getter private final CommandManager<C, T> commandManager;

  protected AbstractCommand(
      @NonNull CommandManager<C, T> commandManager,
      @NonNull List<String> aliases,
      @NonNull List<Middleware<C>> middlewares,
      @NonNull List<Option> options,
      @NonNull List<T> children) {
    this.commandManager = commandManager;
    this.aliases = aliases;
    this.middlewares = middlewares;
    this.options = options;
    this.children = children;
    this.providersRegistry = commandManager.getProvidersRegistry();
    this.messagesProvider = commandManager.getMessagesProvider();
  }

  protected AbstractCommand(
      @NonNull CommandManager<C, T> commandManager, @NonNull Command annotation) {
    this(
        commandManager,
        Arrays.asList(annotation.aliases()),
        commandManager.getMiddlewareRegistry().getMiddlewares(annotation),
        Option.of(annotation.flags()),
        new ArrayList<>());
  }

  @Override
  public boolean hasAlias(@NonNull String alias) {
    for (String commandAlias : aliases) {
      if (commandAlias.equalsIgnoreCase(alias)) {
        return true;
      }
    }
    return false;
  }
}

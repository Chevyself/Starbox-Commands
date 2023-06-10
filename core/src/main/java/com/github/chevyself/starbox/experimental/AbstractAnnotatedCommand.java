package com.github.chevyself.starbox.experimental;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.ReflectCommand;
import com.github.chevyself.starbox.StarboxCommand;
import com.github.chevyself.starbox.StarboxCooldownManager;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.messages.StarboxMessagesProvider;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

public class AbstractAnnotatedCommand<
        C extends StarboxCommandContext, T extends StarboxCommand<C, T>>
    implements ReflectCommand<C, T> {

  @NonNull @Getter private final Object object;
  @NonNull @Getter private final Method method;
  @NonNull @Getter private final List<Argument<?>> arguments;
  @NonNull @Getter private final ProvidersRegistry<C> providersRegistry;
  @NonNull @Getter private final StarboxMessagesProvider<C> messagesProvider;
  @NonNull @Getter private final List<String> aliases;
  @NonNull @Getter private final List<Middleware<C>> middlewares;
  @NonNull @Getter private final List<Option> options;
  @NonNull @Getter private final List<T> children;

  private AbstractAnnotatedCommand(
      @NonNull Object object,
      @NonNull Method method,
      @NonNull List<Argument<?>> arguments,
      @NonNull ProvidersRegistry<C> providersRegistry,
      @NonNull StarboxMessagesProvider<C> messagesProvider,
      @NonNull List<String> aliases,
      @NonNull List<Middleware<C>> middlewares,
      @NonNull List<Option> options,
      @NonNull List<T> children) {
    this.object = object;
    this.method = method;
    this.arguments = arguments;
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
    this.aliases = aliases;
    this.middlewares = middlewares;
    this.options = options;
    this.children = children;
  }

  protected AbstractAnnotatedCommand(
      @NonNull Command annotation,
      @NonNull Object object,
      @NonNull Method method,
      @NonNull CommandManager<C, T> commandManager) {
    this(
        object,
        method,
        Argument.parseArguments(method),
        commandManager.getProvidersRegistry(),
        commandManager.getMessagesProvider(),
        Arrays.asList(annotation.aliases()),
        commandManager.getMiddlewares(annotation),
        Option.of(annotation),
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

  @Override
  public @NonNull Optional<? extends StarboxCooldownManager<C>> getCooldownManager() {
    throw new UnsupportedOperationException("Deprecated");
  }
}

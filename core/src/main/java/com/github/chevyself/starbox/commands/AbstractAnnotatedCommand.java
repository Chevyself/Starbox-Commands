package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.exceptions.MissingArgumentException;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import com.github.chevyself.starbox.result.ArgumentExceptionResult;
import com.github.chevyself.starbox.result.InternalExceptionResult;
import com.github.chevyself.starbox.result.StarboxResult;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

public abstract class AbstractAnnotatedCommand<
        C extends StarboxCommandContext<C, T>, T extends Command<C, T>>
    implements ReflectCommand<C, T> {

  @NonNull @Getter private final Object object;
  @NonNull @Getter private final Method method;
  @NonNull @Getter private final List<Argument<?>> arguments;
  @NonNull @Getter private final ProvidersRegistry<C> providersRegistry;
  @NonNull @Getter private final MessagesProvider<C> messagesProvider;
  @NonNull @Getter private final List<String> aliases;
  @NonNull @Getter private final List<Middleware<C>> middlewares;
  @NonNull @Getter private final List<Option> options;
  @NonNull @Getter private final List<T> children;

  private AbstractAnnotatedCommand(
      @NonNull Object object,
      @NonNull Method method,
      @NonNull List<Argument<?>> arguments,
      @NonNull ProvidersRegistry<C> providersRegistry,
      @NonNull MessagesProvider<C> messagesProvider,
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
      @NonNull com.github.chevyself.starbox.annotations.Command annotation,
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
  public StarboxResult run(@NonNull C context) {
    try {
      Object object = this.method.invoke(this.getObject(), this.getObjects(context));
      if (object instanceof StarboxResult) {
        return (StarboxResult) object;
      } else {
        return null;
      }
    } catch (final IllegalAccessException | InvocationTargetException e) {
      return new InternalExceptionResult(e);
    } catch (MissingArgumentException | ArgumentProviderException e) {
      return new ArgumentExceptionResult(e);
    }
  }

  @Override
  public @NonNull String getName() {
    return aliases.get(0);
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

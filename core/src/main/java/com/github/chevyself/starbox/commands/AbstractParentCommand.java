package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.result.SimpleResult;
import com.github.chevyself.starbox.result.StarboxResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

public abstract class AbstractParentCommand<
        C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>>
    implements StarboxCommand<C, T> {

  @NonNull private final List<String> aliases;
  @NonNull @Getter private final List<Middleware<C>> middlewares;
  @NonNull @Getter private final List<Option> options;
  @NonNull @Getter private final List<T> children;

  public AbstractParentCommand(
      @NonNull List<String> aliases,
      @NonNull List<Middleware<C>> middlewares,
      @NonNull List<Option> options,
      @NonNull List<T> children) {
    this.aliases = aliases;
    this.middlewares = middlewares;
    this.options = options;
    this.children = children;
  }

  public AbstractParentCommand(
      @NonNull Command annotation, @NonNull CommandManager<C, T> commandManager) {
    this(
        Arrays.asList(annotation.aliases()),
        commandManager.getMiddlewareRegistry().getMiddlewares(annotation),
        Option.of(annotation.flags()),
        new ArrayList<>());
  }

  @Override
  public StarboxResult run(@NonNull C context) {
    return new SimpleResult(StarboxCommand.genericHelp(this, this.getChildren()));
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
  public @NonNull String getName() {
    return aliases.get(0);
  }
}

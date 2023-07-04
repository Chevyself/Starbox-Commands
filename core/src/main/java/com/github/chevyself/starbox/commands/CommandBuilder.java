package com.github.chevyself.starbox.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.arguments.ArgumentBuilder;
import com.github.chevyself.starbox.arguments.ArgumentsMap;
import com.github.chevyself.starbox.arguments.SingleArgument;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.metadata.CommandMetadata;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.result.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CommandBuilder<C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  @NonNull
  private final List<String> aliases;
  @NonNull
  private final List<Class<? extends Middleware<C>>> include;
  @NonNull
  private final List<Class<? extends Middleware<C>>> exclude;
  @NonNull
  private final List<Option> options;
  @NonNull
  private final List<T> children;
  @NonNull
  private final List<CommandBuilder<C, T>> extraChildren;
  @NonNull
  private final List<Argument<?>> arguments;
  @NonNull
  private CommandMetadata metadata;
  @NonNull
  private BiFunction<C, ArgumentsMap, Result> executor;

  public CommandBuilder(@NonNull String name) {
    this.aliases = new ArrayList<>();
    this.include = new ArrayList<>();
    this.exclude = new ArrayList<>();
    this.options = new ArrayList<>();
    this.children = new ArrayList<>();
    this.extraChildren = new ArrayList<>();
    this.arguments = new ArrayList<>();
    this.metadata = new CommandMetadata();
    this.executor = (context, arguments) -> null;
    this.aliases.add(name);
  }

  @NonNull
  public CommandBuilder<C, T> executes(@NonNull BiFunction<C, ArgumentsMap, Result> executor) {
    this.executor = executor;
    return this;
  }

  @NonNull
  public CommandBuilder<C, T> argument(@NonNull Argument<?> argument) {
    this.arguments.add(argument);
    return this;
  }

  @NonNull
  public CommandBuilder<C, T> argument(@NonNull ArgumentBuilder builder) {
    return this.argument(builder.build(this.nextArgumentIndex()));
  }

  private int nextArgumentIndex() {
    int index = 0;
    for (Argument<?> argument : this.arguments) {
      if (argument instanceof SingleArgument) {
        index++;
      }
    }
    return index;
  }

  @NonNull
  public CommandBuilder<C, T>  child(@NonNull CommandBuilder<C, T> child) {
    this.extraChildren.add(child);
    return this;
  }

  public void register(@NonNull CommandManager<C, T> commandManager) {
    commandManager.register(this.build(commandManager));
  }

  @NonNull
  public T build(@NonNull CommandManager<C, T> commandManager) {
    return commandManager.getCommandParser().getAdapter().adapt(this);
  }

  public static <C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> CommandBuilder<C, T> literal(@NonNull String name) {
    return new CommandBuilder<>(name);
  }
}

package com.github.chevyself.starbox.experimental;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.StarboxCommand;
import com.github.chevyself.starbox.StarboxCooldownManager;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.result.StarboxResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

public class AbstractParentCommand<C extends StarboxCommandContext, T extends StarboxCommand<C, T>>
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

  public AbstractParentCommand(@NonNull List<String> aliases) {
    this(aliases, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  public AbstractParentCommand(@NonNull String... aliases) {
    this(Arrays.asList(aliases));
  }

  @Override
  public StarboxResult execute(@NonNull C context) {
    return new StarboxResult() {
      @Override
      public @NonNull Optional<String> getMessage() {
        // return Optional.of(context.getMessagesProvider().commandHelp(context, this)));
        return Optional.of("Not implemented yet");
      }
    };
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
    return Optional.empty();
  }
}

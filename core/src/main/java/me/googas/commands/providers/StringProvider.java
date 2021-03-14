package me.googas.commands.providers;

import lombok.NonNull;
import me.googas.commands.ICommandManager;
import me.googas.commands.context.ICommandContext;
import me.googas.commands.providers.type.IArgumentProvider;

/** Provides the {@link ICommandManager} with a {@link String} */
public class StringProvider<T extends ICommandContext> implements IArgumentProvider<String, T> {

  @Override
  public @NonNull Class<String> getClazz() {
    return String.class;
  }

  @NonNull
  @Override
  public String fromString(@NonNull String string, @NonNull T context) {
    return string;
  }
}

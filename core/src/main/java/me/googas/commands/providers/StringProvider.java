package me.googas.commands.providers;

import lombok.NonNull;
import me.googas.commands.EasyCommandManager;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.providers.type.EasyArgumentProvider;

/** Provides the {@link EasyCommandManager} with a {@link String} */
public class StringProvider<T extends EasyCommandContext>
    implements EasyArgumentProvider<String, T> {

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

package me.googas.commands.providers;

import lombok.NonNull;
import me.googas.commands.StarboxCommandManager;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.providers.type.StarboxArgumentProvider;
import me.googas.commands.providers.type.StarboxMultipleArgumentProvider;
import me.googas.starbox.Strings;

/**
 * Provides the {@link StarboxCommandManager} with {@link String}.
 *
 * @param <T> the type of context that this requires to provide the object
 */
public class StringProvider<T extends StarboxCommandContext>
    implements StarboxArgumentProvider<String, T>, StarboxMultipleArgumentProvider<String, T> {

  @Override
  public @NonNull Class<String> getClazz() {
    return String.class;
  }

  @NonNull
  @Override
  public String fromString(@NonNull String string, @NonNull T context) {
    return string;
  }

  @Override
  public @NonNull String fromStrings(@NonNull String[] strings, @NonNull T context) {
    return Strings.fromArray(strings);
  }
}

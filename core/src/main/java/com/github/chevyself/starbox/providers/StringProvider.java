package com.github.chevyself.starbox.providers;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.providers.type.StarboxArgumentProvider;
import lombok.NonNull;

/**
 * Provides the {@link StarboxCommandManager} with {@link String}.
 *
 * @param <T> the type of context that this requires to provide the object
 */
public final class StringProvider<T extends StarboxCommandContext<T, ?>>
    implements StarboxArgumentProvider<String, T> {

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

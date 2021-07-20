package me.googas.commands.providers;

import lombok.NonNull;
import me.googas.commands.StarboxCommandManager;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.objects.JoinedStrings;
import me.googas.commands.providers.type.StarboxMultipleArgumentProvider;

/**
 * Provides the {@link StarboxCommandManager} with a {@link JoinedStrings}
 *
 * @param <T> the type of context that this requires to provide the object
 */
public class JoinedStringsProvider<T extends StarboxCommandContext>
    implements StarboxMultipleArgumentProvider<JoinedStrings, T> {

  @NonNull
  @Override
  public JoinedStrings fromStrings(@NonNull String[] strings, @NonNull T context) {
    return new JoinedStrings(strings);
  }

  @Override
  public @NonNull Class<JoinedStrings> getClazz() {
    return JoinedStrings.class;
  }
}

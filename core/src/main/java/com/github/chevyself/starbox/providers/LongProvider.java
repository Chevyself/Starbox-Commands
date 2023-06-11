package com.github.chevyself.starbox.providers;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.providers.type.StarboxArgumentProvider;
import lombok.NonNull;

/**
 * Provides the {@link StarboxCommandManager} with a {@link Long}.
 *
 * @param <T> the type of context that this requires to provide the object
 */
public final class LongProvider<T extends StarboxCommandContext<T, ?>>
    implements StarboxArgumentProvider<Long, T> {

  private final MessagesProvider<T> messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider to send the error message in case that the {@link Long} could not be
   *     parsed
   */
  public LongProvider(MessagesProvider<T> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Class<Long> getClazz() {
    return Long.class;
  }

  @NonNull
  @Override
  public Long fromString(@NonNull String string, @NonNull T context)
      throws ArgumentProviderException {
    try {
      return Long.parseLong(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException(this.messagesProvider.invalidLong(string, context));
    }
  }

  /**
   * Get if the provider provides with the queried class.
   *
   * @param clazz the queried class
   * @return true if it provides it
   */
  @Override
  public boolean provides(@NonNull Class<?> clazz) {
    return clazz == Long.class || clazz == long.class;
  }
}

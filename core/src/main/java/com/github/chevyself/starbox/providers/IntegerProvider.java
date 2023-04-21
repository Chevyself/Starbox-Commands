package com.github.chevyself.starbox.providers;

import com.github.chevyself.starbox.StarboxCommandManager;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.messages.StarboxMessagesProvider;
import com.github.chevyself.starbox.providers.type.StarboxArgumentProvider;
import lombok.NonNull;

/**
 * Provides the {@link StarboxCommandManager} with a {@link Integer}.
 *
 * @param <T> the type of context that this requires to provide the object
 */
public final class IntegerProvider<T extends StarboxCommandContext>
    implements StarboxArgumentProvider<Integer, T> {

  private final StarboxMessagesProvider<T> messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider to send the error message in case that the {@link Integer} could not be
   *     parsed
   */
  public IntegerProvider(StarboxMessagesProvider<T> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Class<Integer> getClazz() {
    return Integer.class;
  }

  @NonNull
  @Override
  public Integer fromString(@NonNull String string, @NonNull T context)
      throws ArgumentProviderException {
    try {
      return Integer.parseInt(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException(this.messagesProvider.invalidInteger(string, context));
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
    return clazz == Integer.class || clazz == int.class;
  }
}

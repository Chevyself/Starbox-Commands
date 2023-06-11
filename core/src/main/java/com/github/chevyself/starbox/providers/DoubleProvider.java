package com.github.chevyself.starbox.providers;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.messages.StarboxMessagesProvider;
import com.github.chevyself.starbox.providers.type.StarboxArgumentProvider;
import lombok.NonNull;

/**
 * Provides the {@link StarboxCommandManager} with a {@link Double}.
 *
 * @param <T> the type of context that this requires to provide the object
 */
public final class DoubleProvider<T extends StarboxCommandContext<T, ?>>
    implements StarboxArgumentProvider<Double, T> {

  private final StarboxMessagesProvider<T> messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider to send the error message in case that the {@link Double} could not be
   *     parsed
   */
  public DoubleProvider(StarboxMessagesProvider<T> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Class<Double> getClazz() {
    return Double.class;
  }

  @NonNull
  @Override
  public Double fromString(@NonNull String string, @NonNull T context)
      throws ArgumentProviderException {
    try {
      return Double.parseDouble(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException(this.messagesProvider.invalidDouble(string, context));
    }
  }

  @Override
  public boolean provides(@NonNull Class<?> clazz) {
    return clazz == Double.class || clazz == double.class;
  }
}

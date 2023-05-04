package com.github.chevyself.starbox.providers;

import com.github.chevyself.starbox.StarboxCommandManager;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.messages.StarboxMessagesProvider;
import com.github.chevyself.starbox.providers.type.StarboxArgumentProvider;
import lombok.NonNull;

/**
 * Provides the {@link StarboxCommandManager} with a {@link Float}.
 *
 * @param <T> the type of context that this requires to provide the object
 */
public final class FloatProvider<T extends StarboxCommandContext>
    implements StarboxArgumentProvider<Float, T> {

  private final StarboxMessagesProvider<T> messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider to send the error message in case that the {@link Float} could not be
   *     parsed
   */
  public FloatProvider(StarboxMessagesProvider<T> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Class<Float> getClazz() {
    return Float.class;
  }

  @NonNull
  @Override
  public Float fromString(@NonNull String string, @NonNull T context)
      throws ArgumentProviderException {
    try {
      return Float.parseFloat(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException(this.messagesProvider.invalidDouble(string, context));
    }
  }

  @Override
  public boolean provides(@NonNull Class<?> clazz) {
    return clazz == Float.class || clazz == float.class;
  }
}

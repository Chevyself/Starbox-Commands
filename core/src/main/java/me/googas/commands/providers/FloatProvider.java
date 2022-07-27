package me.googas.commands.providers;

import lombok.NonNull;
import me.googas.commands.StarboxCommandManager;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.messages.StarboxMessagesProvider;
import me.googas.commands.providers.type.StarboxArgumentProvider;

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
    return clazz == Double.class || clazz == double.class;
  }
}

package me.googas.commands.providers;

import lombok.NonNull;
import me.googas.commands.EasyCommandManager;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.messages.EasyMessagesProvider;
import me.googas.commands.providers.type.EasyArgumentProvider;

/**
 * Provides the {@link EasyCommandManager} with a {@link Float}
 *
 * @param <T> the type of context that this requires to provide the object
 */
public class FloatProvider<T extends EasyCommandContext> implements EasyArgumentProvider<Float, T> {

  private final EasyMessagesProvider<T> messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the {@link Float} could not be
   *     parsed
   */
  public FloatProvider(EasyMessagesProvider<T> messagesProvider) {
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
      throw new ArgumentProviderException(messagesProvider.invalidDouble(string, context));
    }
  }

  @Override
  public boolean provides(@NonNull Class<?> clazz) {
    return clazz == Double.class || clazz == double.class;
  }
}

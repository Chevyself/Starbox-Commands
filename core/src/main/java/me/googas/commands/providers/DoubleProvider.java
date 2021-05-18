package me.googas.commands.providers;

import lombok.NonNull;
import me.googas.commands.EasyCommandManager;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.messages.EasyMessagesProvider;
import me.googas.commands.providers.type.EasyArgumentProvider;

/**
 * Provides the {@link EasyCommandManager} with a {@link Double}
 *
 * @param <T> the type of context that this requires to provide the object
 */
public class DoubleProvider<T extends EasyCommandContext>
    implements EasyArgumentProvider<Double, T> {

  private final EasyMessagesProvider<T> messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the {@link Double} could not be
   *     parsed
   */
  public DoubleProvider(EasyMessagesProvider<T> messagesProvider) {
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

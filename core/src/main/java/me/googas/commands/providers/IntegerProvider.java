package me.googas.commands.providers;

import lombok.NonNull;
import me.googas.commands.EasyCommandManager;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.messages.EasyMessagesProvider;
import me.googas.commands.providers.type.EasyArgumentProvider;

/**
 * Provides the {@link EasyCommandManager} with a {@link Integer}
 *
 * @param <T> the type of context that this requires to provide the object
 */
public class IntegerProvider<T extends EasyCommandContext>
    implements EasyArgumentProvider<Integer, T> {

  private final EasyMessagesProvider<T> messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the {@link Integer} could not be
   *     parsed
   */
  public IntegerProvider(EasyMessagesProvider<T> messagesProvider) {
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
   * Get if the provider provides with the queried class
   *
   * @param clazz the queried class
   * @return true if it provides it
   */
  @Override
  public boolean provides(@NonNull Class<?> clazz) {
    return clazz == Integer.class || clazz == int.class;
  }
}

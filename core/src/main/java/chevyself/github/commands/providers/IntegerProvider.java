package chevyself.github.commands.providers;

import chevyself.github.commands.StarboxCommandManager;
import chevyself.github.commands.context.StarboxCommandContext;
import chevyself.github.commands.exceptions.ArgumentProviderException;
import chevyself.github.commands.messages.StarboxMessagesProvider;
import chevyself.github.commands.providers.type.StarboxArgumentProvider;
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

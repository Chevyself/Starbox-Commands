package chevyself.github.commands.providers;

import chevyself.github.commands.StarboxCommandManager;
import chevyself.github.commands.context.StarboxCommandContext;
import chevyself.github.commands.exceptions.ArgumentProviderException;
import chevyself.github.commands.messages.StarboxMessagesProvider;
import chevyself.github.commands.providers.type.StarboxArgumentProvider;
import lombok.NonNull;

/**
 * Provides the {@link StarboxCommandManager} with a {@link Long}.
 *
 * @param <T> the type of context that this requires to provide the object
 */
public final class LongProvider<T extends StarboxCommandContext>
    implements StarboxArgumentProvider<Long, T> {

  private final StarboxMessagesProvider<T> messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider to send the error message in case that the {@link Long} could not be
   *     parsed
   */
  public LongProvider(StarboxMessagesProvider<T> messagesProvider) {
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

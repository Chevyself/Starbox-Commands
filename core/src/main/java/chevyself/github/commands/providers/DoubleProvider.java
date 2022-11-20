package chevyself.github.commands.providers;

import chevyself.github.commands.StarboxCommandManager;
import chevyself.github.commands.context.StarboxCommandContext;
import chevyself.github.commands.exceptions.ArgumentProviderException;
import chevyself.github.commands.messages.StarboxMessagesProvider;
import chevyself.github.commands.providers.type.StarboxArgumentProvider;
import lombok.NonNull;

/**
 * Provides the {@link StarboxCommandManager} with a {@link Double}.
 *
 * @param <T> the type of context that this requires to provide the object
 */
public final class DoubleProvider<T extends StarboxCommandContext>
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

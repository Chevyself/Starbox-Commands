package chevyself.github.commands.providers;

import chevyself.github.commands.StarboxCommandManager;
import chevyself.github.commands.context.StarboxCommandContext;
import chevyself.github.commands.exceptions.ArgumentProviderException;
import chevyself.github.commands.messages.StarboxMessagesProvider;
import chevyself.github.commands.providers.type.StarboxArgumentProvider;
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
    return clazz == Double.class || clazz == double.class;
  }
}

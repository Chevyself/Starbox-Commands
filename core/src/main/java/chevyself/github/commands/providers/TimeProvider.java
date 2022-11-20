package chevyself.github.commands.providers;

import chevyself.github.commands.StarboxCommandManager;
import chevyself.github.commands.context.StarboxCommandContext;
import chevyself.github.commands.exceptions.ArgumentProviderException;
import chevyself.github.commands.messages.StarboxMessagesProvider;
import chevyself.github.commands.providers.type.StarboxArgumentProvider;
import chevyself.github.commands.time.Time;
import lombok.NonNull;

/**
 * Provides the {@link StarboxCommandManager} with a {@link Time}.
 *
 * @param <T> the type of context that this requires to provide the object
 */
public final class TimeProvider<T extends StarboxCommandContext>
    implements StarboxArgumentProvider<Time, T> {

  private final StarboxMessagesProvider<T> messagesProvider;

  /**
   * Create an instance of the provider.
   *
   * @param messagesProvider to send the error message in case that the {@link Time} could not be
   *     parsed
   */
  public TimeProvider(StarboxMessagesProvider<T> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Class<Time> getClazz() {
    return Time.class;
  }

  @Override
  public @NonNull Time fromString(@NonNull String string, @NonNull T context)
      throws ArgumentProviderException {
    try {
      return Time.parse(string, true);
    } catch (IllegalArgumentException e) {
      throw new ArgumentProviderException(this.messagesProvider.invalidTime(string, context));
    }
  }
}

package me.googas.commands.providers;

import lombok.NonNull;
import me.googas.commands.StarboxCommandManager;
import me.googas.commands.context.StarboxCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.messages.StarboxMessagesProvider;
import me.googas.commands.providers.type.StarboxArgumentProvider;

/**
 * Provides the {@link StarboxCommandManager} with a {@link Boolean}
 *
 * @param <T> the type of context that this requires to provide the object
 */
public class BooleanProvider<T extends StarboxCommandContext>
    implements StarboxArgumentProvider<Boolean, T> {

  private final StarboxMessagesProvider<T> messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the {@link Boolean} could not be
   *     parsed
   */
  public BooleanProvider(StarboxMessagesProvider<T> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Class<Boolean> getClazz() {
    return boolean.class;
  }

  @NonNull
  @Override
  public Boolean fromString(@NonNull String string, @NonNull T context)
      throws ArgumentProviderException {
    boolean result;
    if (string.equalsIgnoreCase("true")) {
      result = true;
    } else if (string.equals("1")) {
      result = true;
    } else if (string.equalsIgnoreCase("false")) {
      result = false;
    } else if (string.equals("0")) {
      result = false;
    } else {
      throw new ArgumentProviderException(this.messagesProvider.invalidBoolean(string, context));
    }
    return result;
  }

  @Override
  public boolean provides(@NonNull Class<?> clazz) {
    return clazz == Boolean.class || clazz == boolean.class;
  }
}

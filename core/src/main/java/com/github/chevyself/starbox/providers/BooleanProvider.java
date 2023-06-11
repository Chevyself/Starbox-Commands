package com.github.chevyself.starbox.providers;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.messages.StarboxMessagesProvider;
import com.github.chevyself.starbox.providers.type.StarboxArgumentProvider;
import lombok.NonNull;

/**
 * Provides the {@link StarboxCommandManager} with a {@link Boolean}.
 *
 * @param <T> the type of context that this requires to provide the object
 */
public final class BooleanProvider<T extends StarboxCommandContext<T, ?>>
    implements StarboxArgumentProvider<Boolean, T> {

  private final StarboxMessagesProvider<T> messagesProvider;

  /**
   * Create an instance.
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

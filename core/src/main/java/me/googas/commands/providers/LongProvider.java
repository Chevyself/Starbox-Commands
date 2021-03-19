package me.googas.commands.providers;

import lombok.NonNull;
import me.googas.commands.EasyCommandManager;
import me.googas.commands.context.ICommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.messages.IMessagesProvider;
import me.googas.commands.providers.type.IArgumentProvider;

/** Provides the {@link EasyCommandManager} with a {@link Long} */
public class LongProvider<T extends ICommandContext> implements IArgumentProvider<Long, T> {

  private final IMessagesProvider<T> messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public LongProvider(IMessagesProvider<T> messagesProvider) {
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
      throw new ArgumentProviderException(messagesProvider.invalidLong(string, context));
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
    return clazz == Long.class || clazz == long.class;
  }
}

package me.googas.commands.providers;

import lombok.NonNull;
import me.googas.commands.EasyCommandManager;
import me.googas.commands.context.EasyCommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.messages.EasyMessagesProvider;
import me.googas.commands.providers.type.EasyArgumentProvider;
import me.googas.starbox.time.Time;

/**
 * Provides the {@link EasyCommandManager} with a {@link Time}
 *
 * @param <T> the type of context that this requires to provide the object
 */
public class TimeProvider<T extends EasyCommandContext> implements EasyArgumentProvider<Time, T> {

  private final EasyMessagesProvider<T> messagesProvider;

  /**
   * Create an instance of the provider
   *
   * @param messagesProvider to send the error message in case that the {@link Time} could not be
   *     parsed
   */
  public TimeProvider(EasyMessagesProvider<T> messagesProvider) {
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

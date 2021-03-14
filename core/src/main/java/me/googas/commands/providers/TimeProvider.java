package me.googas.commands.providers;

import lombok.NonNull;
import me.googas.commands.ICommandManager;
import me.googas.commands.context.ICommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.messages.IMessagesProvider;
import me.googas.commands.providers.type.IArgumentProvider;
import me.googas.starbox.time.Time;

/** Provides the {@link ICommandManager} with a {@link Time} */
public class TimeProvider<T extends ICommandContext> implements IArgumentProvider<Time, T> {

  private final IMessagesProvider<T> messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public TimeProvider(IMessagesProvider<T> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Class<Time> getClazz() {
    return Time.class;
  }

  @NonNull
  @Override
  public Time fromString(@NonNull String string, @NonNull T context)
      throws ArgumentProviderException {
    try {
      return Time.fromString(string);
    } catch (IllegalArgumentException e) {
      throw new ArgumentProviderException(messagesProvider.invalidTime(string, context));
    }
  }
}

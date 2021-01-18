package com.starfishst.core.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.providers.type.IArgumentProvider;
import lombok.NonNull;
import me.googas.commons.time.Time;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link Time} */
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

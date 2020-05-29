package com.starfishst.core.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.providers.type.IArgumentProvider;
import com.starfishst.core.utils.time.Time;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link Time} */
public class TimeProvider<T extends ICommandContext> implements IArgumentProvider<Time, T> {

  /** The provider to give the error message */
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
  public @NotNull Class<Time> getClazz() {
    return Time.class;
  }

  @NotNull
  @Override
  public Time fromString(@NotNull String string, @NotNull T context)
      throws ArgumentProviderException {
    try {
      return Time.fromString(string);
    } catch (IllegalArgumentException e) {
      throw new ArgumentProviderException(messagesProvider.invalidTime(string, context));
    }
  }
}

package com.starfishst.core.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.providers.type.IArgumentProvider;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link Long} */
public class LongProvider<O, T extends ICommandContext<O>> implements IArgumentProvider<Long, T> {

  /** The provider to give the error message */
  private final IMessagesProvider<O, T> messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public LongProvider(IMessagesProvider<O, T> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NotNull Class<Long> getClazz() {
    return long.class;
  }

  @NotNull
  @Override
  public Long fromString(@NotNull String string, @NotNull T context)
      throws ArgumentProviderException {
    try {
      return Long.parseLong(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException(messagesProvider.invalidLong(string, context));
    }
  }
}

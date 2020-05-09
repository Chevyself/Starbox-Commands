package com.starfishst.core.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.providers.type.IArgumentProvider;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link Integer} */
public class IntegerProvider<O, T extends ICommandContext<O>>
    implements IArgumentProvider<Integer, T> {

  /** The provider to give the error message */
  private final IMessagesProvider<O, T> messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public IntegerProvider(IMessagesProvider<O, T> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NotNull Class<Integer> getClazz() {
    return int.class;
  }

  @NotNull
  @Override
  public Integer fromString(@NotNull String string, @NotNull T context)
      throws ArgumentProviderException {
    try {
      return Integer.parseInt(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException(messagesProvider.invalidInteger(string, context));
    }
  }
}

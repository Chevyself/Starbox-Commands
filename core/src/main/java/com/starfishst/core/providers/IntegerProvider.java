package com.starfishst.core.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.providers.type.IArgumentProvider;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link Integer} */
public class IntegerProvider<T extends ICommandContext> implements IArgumentProvider<Integer, T> {

  /** The provider to give the error message */
  private final IMessagesProvider<T> messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public IntegerProvider(IMessagesProvider<T> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NotNull Class<Integer> getClazz() {
    return Integer.class;
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

  /**
   * Get if the provider provides with the queried class
   *
   * @param clazz the queried class
   * @return true if it provides it
   */
  @Override
  public boolean provides(@NotNull Class<?> clazz) {
    return clazz == Integer.class || clazz == int.class;
  }
}

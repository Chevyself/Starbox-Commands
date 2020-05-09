package com.starfishst.core.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.providers.type.IArgumentProvider;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link Boolean} */
public class BooleanProvider<O, T extends ICommandContext<O>>
    implements IArgumentProvider<Boolean, T> {

  /** The provider to give the error message */
  private final IMessagesProvider<O, T> messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public BooleanProvider(IMessagesProvider<O, T> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NotNull Class<Boolean> getClazz() {
    return boolean.class;
  }

  @NotNull
  @Override
  public Boolean fromString(@NotNull String string, @NotNull T context)
      throws ArgumentProviderException {
    boolean result;
    if (string.equalsIgnoreCase("true")) {
      result = true;
    } else if (string.equalsIgnoreCase("1")) {
      result = true;
    } else if (string.equalsIgnoreCase("false")) {
      result = false;
    } else if (string.equalsIgnoreCase("0")) {
      result = false;
    } else {
      throw new ArgumentProviderException(messagesProvider.invalidBoolean(string, context));
    }
    return result;
  }
}

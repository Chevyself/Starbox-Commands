package com.starfishst.core.providers;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.messages.IMessagesProvider;
import com.starfishst.core.objects.JoinedNumber;
import com.starfishst.core.providers.type.IArgumentProvider;
import org.jetbrains.annotations.NotNull;

public class JoinedNumberProvider<T extends ICommandContext>
    implements IArgumentProvider<JoinedNumber, T> {

  /** The provider to give the error message */
  private final IMessagesProvider<T> messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the number could not be parsed
   */
  public JoinedNumberProvider(IMessagesProvider<T> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NotNull Class<JoinedNumber> getClazz() {
    return JoinedNumber.class;
  }

  @NotNull
  @Override
  public JoinedNumber fromString(@NotNull String string, @NotNull T context)
      throws ArgumentProviderException {
    try {
      return new JoinedNumber(Double.parseDouble(string));
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException(messagesProvider.invalidNumber(string, context));
    }
  }
}

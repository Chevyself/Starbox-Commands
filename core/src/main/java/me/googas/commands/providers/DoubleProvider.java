package me.googas.commands.providers;

import lombok.NonNull;
import me.googas.commands.ICommandManager;
import me.googas.commands.context.ICommandContext;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.messages.IMessagesProvider;
import me.googas.commands.providers.type.IArgumentProvider;

/** Provides the {@link ICommandManager} with a {@link Double} */
public class DoubleProvider<T extends ICommandContext> implements IArgumentProvider<Double, T> {

  private final IMessagesProvider<T> messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public DoubleProvider(IMessagesProvider<T> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Class<Double> getClazz() {
    return Double.class;
  }

  @NonNull
  @Override
  public Double fromString(@NonNull String string, @NonNull T context)
      throws ArgumentProviderException {
    try {
      return Double.parseDouble(string);
    } catch (NumberFormatException e) {
      throw new ArgumentProviderException(messagesProvider.invalidDouble(string, context));
    }
  }

  @Override
  public boolean provides(@NonNull Class<?> clazz) {
    return clazz == Double.class || clazz == double.class;
  }
}

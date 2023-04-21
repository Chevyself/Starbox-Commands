package com.github.chevyself.starbox.providers;

import com.github.chevyself.starbox.StarboxCommandManager;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.exceptions.ArgumentProviderException;
import com.github.chevyself.starbox.messages.StarboxMessagesProvider;
import com.github.chevyself.starbox.providers.type.StarboxArgumentProvider;
import com.github.chevyself.starbox.time.TimeUtil;
import java.time.Duration;
import lombok.NonNull;

/**
 * Provides the {@link StarboxCommandManager} with {@link java.time.Duration}.
 *
 * @param <T> the type of context that this requires to provide the object
 */
public class DurationProvider<T extends StarboxCommandContext>
    implements StarboxArgumentProvider<Duration, T> {

  private final StarboxMessagesProvider<T> messagesProvider;

  /**
   * Create an instance of the provider.
   *
   * @param messagesProvider to send the error message in case that the {@link Duration} could not
   *     be parsed
   */
  public DurationProvider(StarboxMessagesProvider<T> messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @Override
  public @NonNull Duration fromString(@NonNull String string, @NonNull T context)
      throws ArgumentProviderException {
    try {
      return TimeUtil.durationOf(string);
    } catch (IllegalArgumentException e) {
      throw new ArgumentProviderException(this.messagesProvider.invalidDuration(string, context));
    }
  }

  @Override
  public @NonNull Class<Duration> getClazz() {
    return Duration.class;
  }
}

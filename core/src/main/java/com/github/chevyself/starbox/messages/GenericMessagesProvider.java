package com.github.chevyself.starbox.messages;

import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.time.TimeUtil;
import java.time.Duration;
import lombok.NonNull;

/** The default {@link MessagesProvider} for System commands. */
public class GenericMessagesProvider<C extends StarboxCommandContext<C, ?>> implements MessagesProvider<C> {

  @Override
  public @NonNull String invalidLong(@NonNull String string, @NonNull C context) {
    return String.format("%s is not a valid long!", string);
  }

  @Override
  public @NonNull String invalidInteger(@NonNull String string, @NonNull C context) {
    return String.format("%s is not a valid integer!", string);
  }

  @Override
  public @NonNull String invalidDouble(@NonNull String string, @NonNull C context) {
    return String.format("%s is not a valid double!", string);
  }

  @Override
  public @NonNull String invalidBoolean(@NonNull String string, @NonNull C context) {
    return String.format("%s is not a valid boolean!", string);
  }

  @Override
  public @NonNull String invalidDuration(@NonNull String string, @NonNull C context) {
    return String.format("%s is not valid time!", string);
  }

  @Override
  public @NonNull String missingArgument(
      @NonNull String name, @NonNull String description, int position, C context) {
    return String.format("Missing the argument %s (%s) in %d", name, description, position);
  }

  @Override
  public @NonNull String cooldown(@NonNull C context, @NonNull Duration timeLeft) {
    return "You are not allowed to run this command for another " + TimeUtil.toString(timeLeft);
  }
}

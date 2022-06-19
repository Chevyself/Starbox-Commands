package me.googas.commands.system;

import lombok.NonNull;
import me.googas.commands.system.context.CommandContext;
import me.googas.commands.time.Time;

/** The default {@link MessagesProvider} for System commands. */
public class SystemMessagesProvider implements MessagesProvider {
  @Override
  public @NonNull String invalidLong(@NonNull String string, @NonNull CommandContext context) {
    return String.format("%s is not a valid long!", string);
  }

  @Override
  public @NonNull String invalidInteger(@NonNull String string, @NonNull CommandContext context) {
    return String.format("%s is not a valid integer!", string);
  }

  @Override
  public @NonNull String invalidDouble(@NonNull String string, @NonNull CommandContext context) {
    return String.format("%s is not a valid double!", string);
  }

  @Override
  public @NonNull String invalidBoolean(@NonNull String string, @NonNull CommandContext context) {
    return String.format("%s is not a valid boolean!", string);
  }

  @Override
  public @NonNull String invalidTime(@NonNull String string, @NonNull CommandContext context) {
    return String.format("%s is not valid time!", string);
  }

  @Override
  public @NonNull String missingArgument(
      @NonNull String name, @NonNull String description, int position, CommandContext context) {
    return String.format("Missing the argument %s (%s) in %d", name, description, position);
  }

  @Override
  public @NonNull String missingStrings(
      @NonNull String name,
      @NonNull String description,
      int position,
      int minSize,
      int missing,
      @NonNull CommandContext context) {
    return String.format(
        "Missing arguments of %s (%s) in %d missing: %d", name, description, position, missing);
  }

  @Override
  public String cooldown(@NonNull CommandContext context, @NonNull Time timeLeft) {
    return "You are not allowed to run this command for another " + timeLeft;
  }
}

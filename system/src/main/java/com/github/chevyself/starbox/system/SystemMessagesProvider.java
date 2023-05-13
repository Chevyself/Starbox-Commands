package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.StarboxCommand;
import com.github.chevyself.starbox.system.context.CommandContext;
import com.github.chevyself.starbox.time.TimeUtil;
import java.time.Duration;
import java.util.Collection;
import lombok.NonNull;

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
  public @NonNull String invalidDuration(@NonNull String string, @NonNull CommandContext context) {
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
  public @NonNull String cooldown(@NonNull CommandContext context, @NonNull Duration timeLeft) {
    return "You are not allowed to run this command for another " + TimeUtil.toString(timeLeft);
  }

  @Override
  public @NonNull String commandHelp(
      @NonNull StarboxCommand<CommandContext, ?> command, CommandContext context) {
    StringBuilder builder = new StringBuilder(StarboxCommand.generateUsage(command));
    if (command.getChildren().size() > 0 && command instanceof SystemCommand) {
      SystemCommand systemCommand = (SystemCommand) command;
      Collection<SystemCommand> children = systemCommand.getChildren();
      if (children.size() > 0) {
        builder.append("\nSubcommands:");
        for (SystemCommand child : children) {
          builder
              .append("\n + ")
              .append(child.getName())
              .append(" ")
              .append(StarboxCommand.generateUsage(child));
        }
      }
    }
    return builder.toString();
  }
}

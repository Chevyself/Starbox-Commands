package me.googas.commands.bungee.messages;

import lombok.NonNull;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.starbox.Strings;

/** The default messages provider for bungee */
public class BungeeMessagesProvider implements MessagesProvider {

  @Override
  public @NonNull String invalidLong(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid long!";
  }

  @Override
  public @NonNull String invalidInteger(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid integer!";
  }

  @Override
  public @NonNull String invalidDouble(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid double!";
  }

  @Override
  public @NonNull String invalidBoolean(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid boolean!";
  }

  @Override
  public @NonNull String invalidTime(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not valid time!";
  }

  @Override
  public @NonNull String missingArgument(
      @NonNull String name,
      @NonNull String description,
      int position,
      @NonNull CommandContext commandContext) {
    return Strings.format(
        "Missing argument: {0} -> {1}, position: {2}", name, description, position);
  }

  @Override
  public @NonNull String missingStrings(
      @NonNull String name,
      @NonNull String description,
      int position,
      int minSize,
      int missing,
      @NonNull CommandContext context) {
    return "You are missing " + missing + " strings in " + name;
  }

  @NonNull
  @Override
  public String notAllowed(CommandContext context) {
    return "You are not allowed to use this command";
  }

  @Override
  public @NonNull String invalidPlayer(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid player!";
  }

  @Override
  public @NonNull String onlyPlayers(CommandContext context) {
    return "Only players can use this command!";
  }
}

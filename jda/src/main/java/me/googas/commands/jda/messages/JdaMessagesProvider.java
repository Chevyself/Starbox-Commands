package me.googas.commands.jda.messages;

import lombok.NonNull;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.result.ResultType;
import me.googas.commands.utility.Strings;

/** This is a default {@link MessagesProvider} to use if you don't want to create one of your own */
public class JdaMessagesProvider implements MessagesProvider {

  @NonNull
  @Override
  public String invalidLong(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid long";
  }

  @NonNull
  @Override
  public String invalidInteger(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid integer";
  }

  @NonNull
  @Override
  public String invalidDouble(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid double";
  }

  @NonNull
  @Override
  public String invalidBoolean(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid boolean";
  }

  @NonNull
  @Override
  public String invalidTime(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not valid time";
  }

  @Override
  public @NonNull String commandNotFound(@NonNull String command, @NonNull CommandContext context) {
    return "Command not found";
  }

  @Override
  public @NonNull String footer(CommandContext context) {
    return "";
  }

  @Override
  public @NonNull String getTitle(@NonNull ResultType type, CommandContext context) {
    return type.getTitle(null, context);
  }

  @Override
  public @NonNull String response(
      @NonNull String title, @NonNull String message, CommandContext context) {
    return Strings.format("{0} -> {1}", title, message);
  }

  @Override
  public @NonNull String notAllowed(@NonNull CommandContext context) {
    return "You are not allowed to use this command";
  }

  @Override
  public @NonNull String guildOnly(@NonNull CommandContext context) {
    return "You may use this command in a guild";
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

  @Override
  public @NonNull String thumbnailUrl(CommandContext context) {
    return "";
  }

  @Override
  public @NonNull String cooldown(long timeLeft, CommandContext context) {
    return "You are on cooldown! please wait " + timeLeft + "ms";
  }

  @Override
  public @NonNull String invalidUser(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid user";
  }

  @NonNull
  @Override
  public String invalidMember(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid member";
  }

  @NonNull
  @Override
  public String invalidRole(@NonNull String string, @NonNull CommandContext context) {
    return string + " is not a valid role";
  }

  @NonNull
  @Override
  public String invalidTextChannel(String string, CommandContext context) {
    return string + " is not a valid text channel";
  }
}

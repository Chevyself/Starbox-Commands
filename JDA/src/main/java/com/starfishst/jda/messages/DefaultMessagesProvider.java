package com.starfishst.jda.messages;

import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.result.ResultType;
import me.googas.commons.Strings;
import me.googas.commons.time.Time;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This is a default {@link MessagesProvider} to use if you don't want to create one of your own */
public class DefaultMessagesProvider implements MessagesProvider {

  @NotNull
  @Override
  public String invalidLong(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid long";
  }

  @NotNull
  @Override
  public String invalidInteger(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid integer";
  }

  @NotNull
  @Override
  public String invalidDouble(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid double";
  }

  @NotNull
  @Override
  public String invalidBoolean(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid boolean";
  }

  @NotNull
  @Override
  public String invalidTime(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not valid time";
  }

  @Override
  public @NotNull String commandNotFound(@NotNull String command, @NotNull CommandContext context) {
    return "Command not found";
  }

  @Override
  public @NotNull String footer(@NotNull CommandContext context) {
    return "";
  }

  @Override
  public @NotNull String getTitle(@NotNull ResultType type, @NotNull CommandContext context) {
    return type.getTitle(null, context);
  }

  @Override
  public @NotNull String response(
      @NotNull String title, @NotNull String message, @NotNull CommandContext context) {
    return Strings.buildMessage("{0} -> {1}", title, message);
  }

  @Override
  public @NotNull String notAllowed(@NotNull CommandContext context) {
    return "You are not allowed to use this command";
  }

  @Override
  public @NotNull String guildOnly(@NotNull CommandContext context) {
    return "You may use this command in a guild";
  }

  @Override
  public @NotNull String missingArgument(
      @NotNull String name,
      @NotNull String description,
      int position,
      @NotNull CommandContext commandContext) {
    return Strings.buildMessage(
        "Missing argument: {0} -> {1}, position: {2}", name, description, position);
  }

  @Override
  public @NotNull String invalidNumber(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid number";
  }

  @Override
  public @NotNull String emptyDouble(@NotNull CommandContext context) {
    return "Doubles cannot be emtpy!";
  }

  @Override
  public @NotNull String missingStrings(
      @NotNull String name,
      @NotNull String description,
      int position,
      int minSize,
      int missing,
      @NotNull CommandContext context) {
    return "You are missing " + missing + " strings in " + name;
  }

  @Override
  public @NotNull String thumbnailUrl(@Nullable CommandContext context) {
    return "";
  }

  @Override
  public @NotNull String cooldown(@NotNull Time timeLeft, @Nullable CommandContext context) {
    return "You are on cooldown! please wait " + timeLeft.toEffectiveString();
  }

  @Override
  public @NotNull String invalidUser(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid user";
  }

  @NotNull
  @Override
  public String invalidMember(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid member";
  }

  @NotNull
  @Override
  public String invalidRole(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid role";
  }

  @NotNull
  @Override
  public String invalidTextChannel(String string, CommandContext context) {
    return string + " is not a valid text channel";
  }
}

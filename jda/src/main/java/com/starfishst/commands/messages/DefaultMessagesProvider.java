package com.starfishst.commands.messages;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.result.ResultType;
import com.starfishst.core.utils.Strings;
import com.starfishst.core.utils.time.Time;
import org.jetbrains.annotations.NotNull;

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
  public @NotNull String commandNotFound(@NotNull String command) {
    return "Command not found";
  }

  @Override
  public @NotNull String footer() {
    return "";
  }

  @Override
  public @NotNull String getTitle(@NotNull ResultType type) {
    return type.getTitle(null);
  }

  @Override
  public @NotNull String response(@NotNull String title, @NotNull String message) {
    return Strings.buildMessage("{0} -> {1}", title, message);
  }

  @Override
  public @NotNull String notAllowed() {
    return "You are not allowed to use this command";
  }

  @Override
  public @NotNull String guildOnly() {
    return "You may use this command in a guild";
  }

  @Override
  public @NotNull String missingArgument(
      @NotNull String name, @NotNull String description, int position) {
    return Strings.buildMessage(
        "Missing argument: {0} -> {1}, position: {2}", name, description, position);
  }

  @Override
  public @NotNull String thumbnailUrl() {
    return "";
  }

  @Override
  public @NotNull String cooldown(Time timeLeft) {
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

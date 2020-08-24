package com.starfishst.bungee.messages;

import com.starfishst.bungee.context.CommandContext;
import com.starfishst.core.utils.Strings;
import org.jetbrains.annotations.NotNull;

/** The default messages provider for bungee */
public class DefaultMessagesProvider implements MessagesProvider {

  @Override
  public @NotNull String invalidLong(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid long!";
  }

  @Override
  public @NotNull String invalidInteger(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid integer!";
  }

  @Override
  public @NotNull String invalidDouble(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid double!";
  }

  @Override
  public @NotNull String invalidBoolean(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid boolean!";
  }

  @Override
  public @NotNull String invalidTime(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not valid time!";
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
    return "Doubles cannot be empty!";
  }

  @NotNull
  @Override
  public String notAllowed(CommandContext context) {
    return "You are not allowed to use this command";
  }

  /**
   * The message to send when the string does not match a proxied player
   *
   * @param string the string
   * @param context the context of the command
   * @return the message to send
   */
  @Override
  public @NotNull String invalidPlayer(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid player!";
  }

  /**
   * The message to send when a command that is required that the sender is a player happens to be
   * something else
   *
   * @param context the context of the command
   * @return the message
   */
  @Override
  public @NotNull String onlyPlayers(CommandContext context) {
    return "Only players can use this command!";
  }
}

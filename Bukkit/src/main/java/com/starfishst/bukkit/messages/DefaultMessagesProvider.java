package com.starfishst.bukkit.messages;

import com.starfishst.bukkit.AnnotatedCommand;
import com.starfishst.bukkit.ParentCommand;
import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.core.arguments.Argument;
import com.starfishst.core.utils.Strings;
import com.starfishst.core.utils.maps.Maps;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/** The default messages provider for bukkit */
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
      @NotNull CommandContext context) {
    return Strings.buildMessage(
        "Missing argument: {0} -> {1}, position: {2}", name, description, position);
  }

  @Override
  public @NotNull String invalidNumber(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid number!";
  }

  @Override
  public @NotNull String emptyDouble(@NotNull CommandContext context) {
    return "Doubles cannot be empty!";
  }

  @NotNull
  @Override
  public String invalidPlayer(@NotNull String string, @NotNull CommandContext context) {
    return string + " wasn't found";
  }

  @NotNull
  @Override
  public String playersOnly(@NotNull CommandContext context) {
    return "Console cannot use this command";
  }

  @NotNull
  @Override
  public String notAllowed(@NotNull CommandContext context) {
    return "You are not allowed to use this command";
  }

  @Override
  public @NotNull String helpTopicShort(@NotNull Plugin plugin) {
    return "Get help for " + plugin.getName();
  }

  @Override
  public @NotNull String helpTopicFull(
      @NotNull String shortText, @NotNull String commands, @NotNull Plugin plugin) {
    return Strings.buildMessage(
        "&7%short% \n &7Title: &e%name% \n &7Version: &e%version% \n &7Description: &e%description% \n &7Commands (use /help <command>): &e%commands%",
        Maps.builder("short", shortText)
            .append("name", plugin.getName())
            .append(
                "description",
                plugin.getDescription().getDescription() == null
                    ? "None"
                    : plugin.getDescription().getDescription())
            .append("version", plugin.getDescription().getVersion())
            .append("commands", commands));
  }

  @Override
  public @NotNull String helpTopicCommand(@NotNull AnnotatedCommand command) {
    return "&7- &e" + command.getName();
  }

  @Override
  public @NotNull String entitiesOnly(@NotNull CommandContext context) {
    return "&cThis command may only be used by entities!";
  }

  @Override
  public @NotNull String missingArgumentSelectorSeparator(
      @NotNull String argument, @NotNull CommandContext context) {
    return "&c" + argument + " is missing the separator (=)!";
  }

  @Override
  public @NotNull String nullSelectorArgument(
      @NotNull String argument, @NotNull CommandContext context) {
    return "&c" + argument + " must have a value!";
  }

  @Override
  public @NotNull String commandShortText(@NotNull AnnotatedCommand command) {
    return command.getDescription();
  }

  @Override
  public @NotNull String commandName(AnnotatedCommand command) {
    return "/" + command.getName();
  }

  @Override
  public @NotNull String parentCommandFull(
      @NotNull ParentCommand command,
      @NotNull String shortText,
      @NotNull String buildChildren,
      @NotNull String buildArguments) {
    return shortText
        + "\n Permission: "
        + command.getPermission()
        + "\n Usage: "
        + buildArguments
        + "\n Children: "
        + buildChildren;
  }

  @Override
  public @NotNull String parentCommandShort(
      @NotNull ParentCommand command, @NotNull String shortText) {
    return "(Parent) " + shortText;
  }

  @Override
  public @NotNull String commandFull(
      @NotNull AnnotatedCommand command,
      @NotNull String shortText,
      @NotNull String buildArguments) {
    return shortText + "\n Permission: " + command.getPermission() + "\n Usage: " + buildArguments;
  }

  @Override
  public @NotNull String childCommandName(
      @NotNull AnnotatedCommand command, @NotNull ParentCommand parent) {
    return "/" + parent.getName() + "." + command.getName();
  }

  @Override
  public @NotNull String childCommandShort(
      @NotNull AnnotatedCommand command, @NotNull ParentCommand parent) {
    return command.getDescription();
  }

  @Override
  public @NotNull String childCommandFull(
      @NotNull AnnotatedCommand command,
      @NotNull ParentCommand parent,
      @NotNull String shortText,
      @NotNull String buildArguments) {
    return shortText
        + "\n Permission: "
        + command.getPermission()
        + "\n Usage: "
        + buildArguments
        + "\n Parent: "
        + commandName(command);
  }

  @Override
  public @NotNull String requiredArgumentHelp(@NotNull Argument<?> argument) {
    return "- <" + argument.getName() + "> " + argument.getDescription() + "\n";
  }

  @Override
  public @NotNull String optionalArgumentHelp(@NotNull Argument<?> argument) {
    return "- (" + argument.getName() + ") " + argument.getDescription() + "\n";
  }

  @Override
  public @NotNull String childCommand(
      @NotNull AnnotatedCommand command, @NotNull ParentCommand parent) {
    return "- " + command.getName();
  }

  @Override
  public @NotNull String invalidMaterialEmpty(@NotNull CommandContext context) {
    return "&cThe name of materials cannot be empty!";
  }

  @Override
  public @NotNull String invalidMaterial(@NotNull String string, @NotNull CommandContext context) {
    return string + " is not a valid material!";
  }
}

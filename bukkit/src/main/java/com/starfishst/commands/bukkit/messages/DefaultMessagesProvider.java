package com.starfishst.commands.bukkit.messages;

import com.starfishst.commands.bukkit.AnnotatedCommand;
import com.starfishst.commands.bukkit.ParentCommand;
import com.starfishst.commands.bukkit.context.CommandContext;
import com.starfishst.commands.bukkit.utils.BukkitUtils;
import me.googas.commands.arguments.Argument;
import lombok.NonNull;
import me.googas.commons.maps.Maps;
import org.bukkit.plugin.Plugin;

/** The default messages provider for bukkit */
public class DefaultMessagesProvider implements MessagesProvider {

  @Override
  public @NonNull String invalidLong(@NonNull String string, @NonNull CommandContext context) {
    return BukkitUtils.build("&e" + string + " &cis not a valid long");
  }

  @Override
  public @NonNull String invalidInteger(@NonNull String string, @NonNull CommandContext context) {
    return BukkitUtils.build("&e" + string + " &cis not a valid integer");
  }

  @Override
  public @NonNull String invalidDouble(@NonNull String string, @NonNull CommandContext context) {
    return BukkitUtils.build("&e" + string + " &cis not a valid double");
  }

  @Override
  public @NonNull String invalidBoolean(@NonNull String string, @NonNull CommandContext context) {
    return BukkitUtils.build("&e" + string + " &cis not a valid boolean");
  }

  @Override
  public @NonNull String invalidTime(@NonNull String string, @NonNull CommandContext context) {
    return BukkitUtils.build("&e" + string + " &cis not valid time");
  }

  @Override
  public @NonNull String missingArgument(
      @NonNull String name,
      @NonNull String description,
      int position,
      @NonNull CommandContext context) {
    return BukkitUtils.build(
        "&cMissing argument: &e%name% &c-> &e%description%&c, position: &e%position%",
        Maps.builder("name", name)
            .append("description", description)
            .append("position", String.valueOf(position))
            .build());
  }

  @Override
  public @NonNull String missingStrings(
      @NonNull String name,
      @NonNull String description,
      int position,
      int minSize,
      int missing,
      @NonNull CommandContext context) {
    return BukkitUtils.build("&cYou are missing &e" + missing + " &cstrings in &e" + name);
  }

  @NonNull
  @Override
  public String invalidPlayer(@NonNull String string, @NonNull CommandContext context) {
    return BukkitUtils.build("&e" + string + "  &cisn't online");
  }

  @NonNull
  @Override
  public String playersOnly(@NonNull CommandContext context) {
    return BukkitUtils.build("&cConsole cannot use this command");
  }

  @NonNull
  @Override
  public String notAllowed(@NonNull CommandContext context) {
    return BukkitUtils.build("&cYou are not allowed to use this command");
  }

  @Override
  public @NonNull String helpTopicShort(@NonNull Plugin plugin) {
    return "Get help for " + plugin.getName();
  }

  @Override
  public @NonNull String helpTopicFull(
      @NonNull String shortText, @NonNull String commands, @NonNull Plugin plugin) {
    return BukkitUtils.build(
        "&7%short% \n &7Title: &e%name% \n &7Version: &e%version% \n &7Description: &e%description% \n &7Commands (use /help <command>): &e%commands%",
        Maps.builder("short", shortText)
            .append("name", plugin.getName())
            .append(
                "description",
                plugin.getDescription().getDescription() == null
                    ? "None"
                    : plugin.getDescription().getDescription())
            .append("version", plugin.getDescription().getVersion())
            .append("commands", commands)
            .build());
  }

  @Override
  public @NonNull String helpTopicCommand(@NonNull AnnotatedCommand command) {
    return BukkitUtils.build("&7- &e" + command.getName());
  }

  @Override
  public @NonNull String commandShortText(@NonNull AnnotatedCommand command) {
    return command.getDescription();
  }

  @Override
  public @NonNull String commandName(AnnotatedCommand command) {
    return "/" + command.getName();
  }

  @Override
  public @NonNull String parentCommandFull(
      @NonNull ParentCommand command,
      @NonNull String shortText,
      @NonNull String buildChildren,
      @NonNull String buildArguments) {
    return shortText
        + "\n Permission: "
        + command.getPermission()
        + "\n Usage: "
        + buildArguments
        + "\n Children: "
        + buildChildren;
  }

  @Override
  public @NonNull String parentCommandShort(
      @NonNull ParentCommand command, @NonNull String shortText) {
    return "(Parent) " + shortText;
  }

  @Override
  public @NonNull String commandFull(
      @NonNull AnnotatedCommand command,
      @NonNull String shortText,
      @NonNull String buildArguments) {
    return shortText + "\n Permission: " + command.getPermission() + "\n Usage: " + buildArguments;
  }

  @Override
  public @NonNull String childCommandName(
      @NonNull AnnotatedCommand command, @NonNull ParentCommand parent) {
    return "/" + parent.getName() + "." + command.getName();
  }

  @Override
  public @NonNull String childCommandShort(
      @NonNull AnnotatedCommand command, @NonNull ParentCommand parent) {
    return command.getDescription();
  }

  @Override
  public @NonNull String childCommandFull(
      @NonNull AnnotatedCommand command,
      @NonNull ParentCommand parent,
      @NonNull String shortText,
      @NonNull String buildArguments) {
    return shortText
        + "\n Permission: "
        + command.getPermission()
        + "\n Usage: "
        + buildArguments
        + "\n Parent: "
        + commandName(command);
  }

  @Override
  public @NonNull String requiredArgumentHelp(@NonNull Argument<?> argument) {
    return "- <" + argument.getName() + "> " + argument.getDescription() + "\n";
  }

  @Override
  public @NonNull String optionalArgumentHelp(@NonNull Argument<?> argument) {
    return "- (" + argument.getName() + ") " + argument.getDescription() + "\n";
  }

  @Override
  public @NonNull String childCommand(
      @NonNull AnnotatedCommand command, @NonNull ParentCommand parent) {
    return "- " + command.getName();
  }

  @Override
  public @NonNull String invalidMaterialEmpty(@NonNull CommandContext context) {
    return BukkitUtils.build("&cThe name of materials cannot be empty!");
  }

  @Override
  public @NonNull String invalidMaterial(@NonNull String string, @NonNull CommandContext context) {
    return BukkitUtils.build("&e" + string + " &cis not a valid material!");
  }
}

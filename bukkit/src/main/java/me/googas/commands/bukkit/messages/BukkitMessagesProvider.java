package me.googas.commands.bukkit.messages;

import lombok.NonNull;
import me.googas.commands.bukkit.EasyBukkitCommand;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.utility.Maps;
import org.bukkit.plugin.Plugin;

/** The default messages provider for bukkit */
public class BukkitMessagesProvider implements MessagesProvider {

  @Override
  public @NonNull String invalidLong(@NonNull String string, @NonNull CommandContext context) {
    return BukkitUtils.format("&e" + string + " &cis not a valid long");
  }

  @Override
  public @NonNull String invalidInteger(@NonNull String string, @NonNull CommandContext context) {
    return BukkitUtils.format("&e" + string + " &cis not a valid integer");
  }

  @Override
  public @NonNull String invalidDouble(@NonNull String string, @NonNull CommandContext context) {
    return BukkitUtils.format("&e" + string + " &cis not a valid double");
  }

  @Override
  public @NonNull String invalidBoolean(@NonNull String string, @NonNull CommandContext context) {
    return BukkitUtils.format("&e" + string + " &cis not a valid boolean");
  }

  @Override
  public @NonNull String invalidTime(@NonNull String string, @NonNull CommandContext context) {
    return BukkitUtils.format("&e" + string + " &cis not valid time");
  }

  @Override
  public @NonNull String missingArgument(
      @NonNull String name,
      @NonNull String description,
      int position,
      @NonNull CommandContext context) {
    return BukkitUtils.format(
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
    return BukkitUtils.format("&cYou are missing &e" + missing + " &cstrings in &e" + name);
  }

  @NonNull
  @Override
  public String invalidPlayer(@NonNull String string, @NonNull CommandContext context) {
    return BukkitUtils.format("&e" + string + "  &cisn't online");
  }

  @NonNull
  @Override
  public String playersOnly(@NonNull CommandContext context) {
    return BukkitUtils.format("&cConsole cannot use this command");
  }

  @NonNull
  @Override
  public String notAllowed(@NonNull CommandContext context) {
    return BukkitUtils.format("&cYou are not allowed to use this command");
  }

  @Override
  public @NonNull String helpTopicShort(@NonNull Plugin plugin) {
    return "Get help for " + plugin.getName();
  }

  @Override
  public @NonNull String helpTopicFull(
      @NonNull String shortText, @NonNull String commands, @NonNull Plugin plugin) {
    return BukkitUtils.format(
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
  public @NonNull String helpTopicCommand(@NonNull EasyBukkitCommand command) {
    return BukkitUtils.format("&7- &e" + command.getName());
  }

  @Override
  public @NonNull String commandShortText(@NonNull EasyBukkitCommand command) {
    return command.getDescription();
  }

  @Override
  public @NonNull String commandName(@NonNull EasyBukkitCommand command, String parentName) {
    return "/" + (parentName == null ? command.getName() : parentName + "." + command.getName());
  }

  @Override
  public @NonNull String commandFullText(
      @NonNull EasyBukkitCommand command,
      @NonNull String shortText,
      @NonNull String buildChildren,
      @NonNull String buildArguments) {
    String full =
        shortText + "\n Permission: " + command.getPermission() + "\n Usage: " + buildArguments;
    return buildChildren.isEmpty() ? full : full + "\n Children: " + buildChildren;
  }

  @Override
  public @NonNull String childCommand(
      @NonNull EasyBukkitCommand command, @NonNull EasyBukkitCommand parent) {
    return "\n - " + command.getName();
  }

  @Override
  public @NonNull String invalidMaterialEmpty(@NonNull CommandContext context) {
    return BukkitUtils.format("&cThe name of materials cannot be empty!");
  }

  @Override
  public @NonNull String invalidMaterial(@NonNull String string, @NonNull CommandContext context) {
    return BukkitUtils.format("&e" + string + " &cis not a valid material!");
  }
}

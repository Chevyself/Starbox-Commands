package com.github.chevyself.starbox.bukkit.messages;

import com.github.chevyself.starbox.StarboxCommand;
import com.github.chevyself.starbox.bukkit.StarboxBukkitCommand;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.utils.BukkitUtils;
import com.github.chevyself.starbox.time.TimeUtil;
import com.github.chevyself.starbox.util.Strings;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;

/** The default {@link MessagesProvider} for Bukkit. */
public class BukkitMessagesProvider implements MessagesProvider {

  @NonNull public static final String ERROR_PREFIX = "&e&o⚠ &r";

  @Override
  public @NonNull String invalidLong(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format(
        BukkitMessagesProvider.ERROR_PREFIX + "&4&o{0} &c&ois not a valid long", string);
  }

  @Override
  public @NonNull String invalidInteger(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format(
        BukkitMessagesProvider.ERROR_PREFIX + "&4&o{0} &c&ois not a valid integer", string);
  }

  @Override
  public @NonNull String invalidDouble(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format(
        BukkitMessagesProvider.ERROR_PREFIX + "&4&o{0} &c&ois not a valid double", string);
  }

  @Override
  public @NonNull String invalidBoolean(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format(
        BukkitMessagesProvider.ERROR_PREFIX + "&4&o{0} &c&ois not a valid boolean", string);
  }

  @Override
  public @NonNull String invalidDuration(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format(
        BukkitMessagesProvider.ERROR_PREFIX + "&4&o{0} &c&ois not valid time", string);
  }

  @Override
  public @NonNull String missingArgument(
      @NonNull String name,
      @NonNull String description,
      int position,
      @NonNull CommandContext context) {
    return BukkitMessagesProvider.ERROR_PREFIX
        + "&c&oYou are missing the argument &4&o"
        + name
        + "&c&o at position &4&o"
        + position
        + "&c&o: &7&o"
        + description;
  }

  @Override
  public @NonNull String cooldown(@NonNull CommandContext context, @NonNull Duration timeLeft) {
    return BukkitMessagesProvider.ERROR_PREFIX
        + "&c&oYou will be allowed to run this command in &4&o"
        + TimeUtil.toString(timeLeft);
  }

  @Override
  public @NonNull String commandHelp(
      @NonNull StarboxCommand<CommandContext, ?> command, CommandContext context) {
    if (command instanceof StarboxBukkitCommand) {
      StarboxBukkitCommand bukkitCommand = (StarboxBukkitCommand) command;
      return "&c"
          + StarboxCommand.genericHelp(
              bukkitCommand, bukkitCommand.getChildren(), Command::getName);
    }
    return "&cUnknown command";
  }

  @NonNull
  @Override
  public String invalidPlayer(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format(
        BukkitMessagesProvider.ERROR_PREFIX + "&4&o{0} &c&ois not a valid player", string);
  }

  @NonNull
  @Override
  public String playersOnly(@NonNull CommandContext context) {
    return BukkitMessagesProvider.ERROR_PREFIX + "&c&oConsole cannot use this command";
  }

  @NonNull
  @Override
  public String notAllowed(@NonNull CommandContext context) {
    return BukkitMessagesProvider.ERROR_PREFIX + "&c&oYou are not allowed to use this command";
  }

  @Override
  public @NonNull String helpTopicShort(@NonNull Plugin plugin) {
    return "Get help for " + plugin.getName();
  }

  @Override
  public @NonNull String helpTopicFull(
      @NonNull String shortText, @NonNull String commands, @NonNull Plugin plugin) {
    String description =
        plugin.getDescription().getDescription() == null
            ? "No description given"
            : plugin.getDescription().getDescription();
    return BukkitUtils.format(
        "&6Version: &f{0} \n &6Description: &f{1} \n &7Commands (use /help <command>): {2}",
        plugin.getDescription().getVersion(), description, commands);
  }

  @Override
  public @NonNull String helpTopicCommand(@NonNull StarboxBukkitCommand command) {
    List<String> aliases = new ArrayList<>(command.getAliases());
    aliases.add(command.getName());
    return BukkitUtils.format(
        "\n&6/{0}: &f{1}", Strings.buildUsageAliases(aliases), command.getDescription());
  }

  @Override
  public @NonNull String commandShortText(@NonNull StarboxBukkitCommand command) {
    return command.getDescription();
  }

  @Override
  public @NonNull String commandName(@NonNull StarboxBukkitCommand command, String parentName) {
    return (parentName == null
        ? "/" + command.getName()
        : (parentName.startsWith("/") ? "" : "/") + parentName + "." + command.getName());
  }

  @Override
  public @NonNull String commandFullText(
      @NonNull StarboxBukkitCommand command, @NonNull String builtChildren) {
    StringBuilder builder =
        new StringBuilder()
            .append("&6Description: &f")
            .append(command.getDescription())
            .append("\n&6Usage: &f")
            .append(command.getUsage());
    if (command.getPermission() != null) {
      builder.append("\n&6Permission: &f").append(command.getPermission());
    }
    if (!command.getChildren().isEmpty()) {
      builder.append("\n&6Children: &r").append(builtChildren);
    }
    return BukkitUtils.format(builder.toString());
  }

  @Override
  public @NonNull String childCommand(
      @NonNull StarboxBukkitCommand command, @NonNull StarboxBukkitCommand parent) {
    return BukkitUtils.format(
        "\n&6/{0} {1}: &f{2}", parent.getName(), command.getName(), command.getDescription());
  }

  @Override
  public @NonNull String invalidMaterialEmpty(@NonNull CommandContext context) {
    return BukkitMessagesProvider.ERROR_PREFIX + "&c&oMaterial cannot be empty";
  }

  @Override
  public @NonNull String invalidMaterial(@NonNull String string, @NonNull CommandContext context) {
    return Strings.format(
        BukkitMessagesProvider.ERROR_PREFIX + "&4&o{0} &c&ois not a valid material", string);
  }
}

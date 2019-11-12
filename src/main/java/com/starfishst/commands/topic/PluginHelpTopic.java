package com.starfishst.commands.topic;

import com.starfishst.commands.CommandManager;
import com.starfishst.commands.utils.Strings;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class PluginHelpTopic extends HelpTopic {

  public PluginHelpTopic(@NotNull CommandManager manager, @NotNull Plugin plugin) {
    final PluginDescriptionFile description = plugin.getDescription();
    String TEMPLATE =
        "&7{0} \n &7Title: &e{1} \n &7Version: &e{2} \n &7Description: &e{3} \n &7Commands (use /help <command>): &e{4}";
    this.amendedPermission = plugin.getName() + ".help";
    this.name = plugin.getName();
    this.shortText = "Get help for " + plugin.getName();
    this.fullText =
        Strings.getMessage(
            TEMPLATE,
            shortText,
            plugin.getName(),
            description.getVersion(),
            description.getDescription(),
            getCommands(manager));
  }

  @NotNull
  private String getCommands(CommandManager manager) {
    StringBuilder builder = Strings.getBuilder();
    manager
        .getCommands()
        .forEach(
            command ->
                builder.append(Strings.getMessage("&7- &e{0}", command.getName())).append("\n"));
    return builder.toString();
  }

  @Override
  public boolean canSee(@NotNull CommandSender commandSender) {
    return commandSender.hasPermission(this.amendedPermission);
  }
}

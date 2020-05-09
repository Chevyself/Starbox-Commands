package com.starfishst.bukkit.topic;

import com.starfishst.bukkit.CommandManager;
import com.starfishst.bukkit.utils.BukkitUtils;
import com.starfishst.core.utils.Strings;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

/** The help topic for the plugin that is using the framework */
public class PluginHelpTopic extends HelpTopic {

  /**
   * Create the help topic
   *
   * @param plugin the plugin that is using the framework
   */
  public PluginHelpTopic(@NotNull Plugin plugin) {
    final PluginDescriptionFile description = plugin.getDescription();
    String TEMPLATE =
        "&7{0} \n &7Title: &e{1} \n &7Version: &e{2} \n &7Description: &e{3} \n &7Commands (use /help <command>): &e{4}";
    this.amendedPermission = plugin.getName() + ".help";
    this.name = plugin.getName();
    this.shortText = "Get help for " + plugin.getName();
    this.fullText =
        BukkitUtils.getMessage(
            TEMPLATE,
            this.shortText,
            plugin.getName(),
            description.getVersion(),
            description.getDescription(),
            this.getCommands());
  }

  /**
   * Get all the commands that the plugin has
   *
   * @return the commands that the plugin has
   */
  @NotNull
  private String getCommands() {
    StringBuilder builder = Strings.getBuilder();
    CommandManager.getCommands()
        .forEach(
            command ->
                builder
                    .append(BukkitUtils.getMessage("&7- &e{0}", command.getName()))
                    .append("\n"));
    return builder.toString();
  }

  @Override
  public boolean canSee(@NotNull CommandSender commandSender) {
    return commandSender.hasPermission(this.amendedPermission);
  }
}

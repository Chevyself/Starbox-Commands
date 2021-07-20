package me.googas.commands.bukkit.topic;

import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.bukkit.CommandManager;
import me.googas.commands.bukkit.StarboxBukkitCommand;
import me.googas.commands.bukkit.messages.MessagesProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.Plugin;

/**
 * Plugin {@link HelpTopic} is basically the messages that appear in the command '/help
 * &lt;plugin_name&gt;'. This helps creating a {@link HelpTopic} for {@link
 * org.bukkit.plugin.java.JavaPlugin} that uses {@link CommandManager} to parseAndRegister commands
 */
public class PluginHelpTopic extends HelpTopic {

  @NonNull @Getter private final CommandManager manager;
  @NonNull @Getter private final MessagesProvider provider;

  /**
   * Create the an instance of the help topic
   *
   * @param plugin the plugin that is using the framework
   * @param manager the command manager that the plugin uses
   * @param provider the messages provider to format the messages see {@link
   *     MessagesProvider#helpTopicShort(Plugin)}, {@link MessagesProvider#helpTopicFull(String,
   *     String, Plugin)}
   */
  public PluginHelpTopic(
      @NonNull Plugin plugin, @NonNull CommandManager manager, @NonNull MessagesProvider provider) {
    this.manager = manager;
    this.provider = provider;
    this.amendedPermission = plugin.getName().replace(" ", "_") + ".help";
    this.name = plugin.getName();
    this.shortText = this.provider.helpTopicShort(plugin);
    this.fullText = this.provider.helpTopicFull(this.shortText, this.getCommands(), plugin);
  }

  /**
   * Gets the message to include in {@link MessagesProvider#helpTopicFull(String, String, Plugin)},
   * Using a {@link StringBuilder} getting all the commands registered in {@link #manager} and
   * adding its {@link MessagesProvider#helpTopicCommand(StarboxBukkitCommand)} to the builder.
   *
   * @return the message that includes the commands for {@link
   *     MessagesProvider#helpTopicFull(String, String, Plugin)}}
   */
  @NonNull
  private String getCommands() {
    StringBuilder builder = new StringBuilder();
    for (StarboxBukkitCommand command : this.manager.getCommands()) {
      builder.append(this.provider.helpTopicCommand(command));
    }
    return builder.toString();
  }

  @Override
  public boolean canSee(@NonNull CommandSender commandSender) {
    return commandSender.hasPermission(this.amendedPermission);
  }
}

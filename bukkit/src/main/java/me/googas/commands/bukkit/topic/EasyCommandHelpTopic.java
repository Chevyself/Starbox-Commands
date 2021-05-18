package me.googas.commands.bukkit.topic;

import java.util.Collection;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.commands.EasyCommand;
import me.googas.commands.bukkit.EasyBukkitCommand;
import me.googas.commands.bukkit.messages.MessagesProvider;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;

/**
 * Command {@link HelpTopic} is basically the messages that appear in the command '/help
 * <command_name>'. This helps creating a {@link HelpTopic} for {@link EasyCommandHelpTopic}
 *
 * <p>To know how this is formatted check {@link MessagesProvider} and also the constructor {@link
 * #EasyCommandHelpTopic(EasyBukkitCommand, EasyCommandHelpTopic, MessagesProvider)} has more
 * detailed information about the {@link HelpTopic} {}
 */
class EasyCommandHelpTopic extends HelpTopic {

  @NonNull private static final Server server = Bukkit.getServer();
  /** This instance of {@link HelpMap} is used to parseAndRegister topics for children commands */
  @NonNull private static final HelpMap helpMap = EasyCommandHelpTopic.server.getHelpMap();

  @NonNull @Getter @Setter private MessagesProvider provider;

  /**
   * Create the topic. The topic is created with {@link MessagesProvider}:
   *
   * <ul>
   *   <li>The name of the command = {@link MessagesProvider#commandName(EasyBukkitCommand, String)}
   *   <li>The short text/summary of the command = {@link
   *       MessagesProvider#commandShortText(EasyBukkitCommand)}
   *   <li>The full description of the command = {@link
   *       MessagesProvider#commandFullText(EasyBukkitCommand, String, String, String)}
   * </ul>
   *
   * For each children inside the command {@link EasyCommand#getChildren()} another {@link
   * EasyCommandHelpTopic} will be created
   *
   * @param command the command to create the topic from
   * @param parent the parent if the command has one
   * @param provider the messages provider to format messages for the help topic
   */
  EasyCommandHelpTopic(
      @NonNull EasyBukkitCommand command,
      EasyCommandHelpTopic parent,
      @NonNull MessagesProvider provider) {
    this.provider = provider;
    this.amendedPermission = getAmendedPermission(command);
    this.name = provider.commandName(command, parent == null ? null : parent.getName());
    this.shortText = provider.commandShortText(command);
    this.fullText =
        provider.commandFullText(
            command, this.shortText, this.buildChildren(command), command.getUsage());
    for (EasyBukkitCommand child : command.getChildren()) {
      helpMap.addTopic(new EasyCommandHelpTopic(child, this, provider));
    }
  }

  /**
   * Get the permission that is used for the command {@link HelpTopic} in {@link
   * EasyCommandHelpTopic}
   *
   * @param command the command to get the permission from
   * @return the permission of the command
   */
  public static String getAmendedPermission(@NonNull EasyBukkitCommand command) {
    String node = command.getPermission();
    return node == null ? null : node.isEmpty() ? null : node;
  }

  /**
   * Build the children help. This will use a {@link StringBuilder} and append {@link
   * MessagesProvider#childCommand(EasyBukkitCommand, EasyBukkitCommand)}
   *
   * @param command the parent to get the children help from
   * @return the help of the children command as string
   */
  @NonNull
  private String buildChildren(@NonNull EasyBukkitCommand command) {
    StringBuilder builder = new StringBuilder();
    final Collection<EasyBukkitCommand> commands = command.getChildren();
    for (EasyBukkitCommand child : commands) {
      builder.append(provider.childCommand(child, command));
    }
    return builder.toString();
  }

  @Override
  public boolean canSee(@NonNull CommandSender commandSender) {
    if (this.amendedPermission == null) {
      return true;
    } else {
      return commandSender.hasPermission(this.amendedPermission);
    }
  }
}

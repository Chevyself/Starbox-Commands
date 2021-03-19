package me.googas.commands.bukkit.topic;

import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.commands.bukkit.AnnotatedParentCommand;
import me.googas.commands.bukkit.BukkitCommand;
import me.googas.commands.bukkit.messages.MessagesProvider;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;

/** The {@link HelpTopic} for the commands generated with annotations */
class AnnotatedCommandHelpTopic extends HelpTopic {

  /** The server to get the {@link HelpMap} from */
  @NonNull private static final Server server = Bukkit.getServer();
  /** The HelpMap to register the command */
  @NonNull private static final HelpMap helpMap = AnnotatedCommandHelpTopic.server.getHelpMap();

  /** The messages provider to format messages for the help topic */
  @NonNull @Getter @Setter private MessagesProvider provider;

  /**
   * Create the topic
   *
   * @param command the command to create the topic from
   * @param parent the parent if the command has one
   * @param provider the messages provider to format messages for the help topic
   */
  AnnotatedCommandHelpTopic(
      @NonNull BukkitCommand command,
      AnnotatedParentCommand parent,
      @NonNull MessagesProvider provider) {
    this.provider = provider;
    final String permission = command.getPermission();
    this.amendedPermission = permission == null ? null : permission.isEmpty() ? null : permission;
    if (parent == null) {
      this.shortText = provider.commandShortText(command);
      this.name = provider.commandName(command);
      if (command instanceof AnnotatedParentCommand) {
        this.fullText =
            provider.parentCommandFull(
                (AnnotatedParentCommand) command,
                provider.parentCommandShort((AnnotatedParentCommand) command, this.shortText),
                this.buildChildren((AnnotatedParentCommand) command),
                buildUsage(provider, command));
        final List<BukkitCommand> commands = ((AnnotatedParentCommand) command).getChildren();
        commands.forEach(
            childCommand ->
                AnnotatedCommandHelpTopic.helpMap.addTopic(
                    new AnnotatedCommandHelpTopic(
                        childCommand, (AnnotatedParentCommand) command, provider)));
      } else {
        this.fullText =
            provider.commandFull(command, this.shortText, buildUsage(provider, command));
      }
    } else {
      this.name = provider.childCommandName(command, parent);
      this.shortText = provider.childCommandShort(command, parent);
      this.fullText =
          provider.childCommandFull(command, parent, this.shortText, buildUsage(provider, command));
    }
  }

  /**
   * Build the arguments help
   *
   * @param provider the messages provider to format the help topic
   * @param command the command to format the arguments from
   * @return the arguments built help
   */
  @NonNull
  private static String buildUsage(
      @NonNull MessagesProvider provider, @NonNull BukkitCommand command) {
    return command.getUsage();
  }

  /**
   * Build the children help
   *
   * @param command the parent to get the children help from
   * @return the help as string
   */
  @NonNull
  private String buildChildren(@NonNull AnnotatedParentCommand command) {
    StringBuilder builder = new StringBuilder();
    final List<BukkitCommand> commands = command.getChildren();
    commands.forEach(
        annotatedCommand -> builder.append(provider.childCommand(annotatedCommand, command)));
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

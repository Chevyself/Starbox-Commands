package me.googas.commands.bukkit.topic;

import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.commands.arguments.Argument;
import me.googas.commands.bukkit.AnnotatedCommand;
import me.googas.commands.bukkit.ParentCommand;
import me.googas.commands.bukkit.messages.MessagesProvider;
import me.googas.starbox.Strings;
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

  /** The messages provider to build messages for the help topic */
  @NonNull @Getter @Setter private MessagesProvider provider;

  /**
   * Create the topic
   *
   * @param command the command to create the topic from
   * @param parent the parent if the command has one
   * @param provider the messages provider to build messages for the help topic
   */
  AnnotatedCommandHelpTopic(
      @NonNull AnnotatedCommand command, ParentCommand parent, @NonNull MessagesProvider provider) {
    this.provider = provider;
    final String permission = command.getPermission();
    this.amendedPermission = permission == null ? null : permission.isEmpty() ? null : permission;
    if (parent == null) {
      this.shortText = provider.commandShortText(command);
      this.name = provider.commandName(command);
      if (command instanceof ParentCommand) {
        this.fullText =
            provider.parentCommandFull(
                (ParentCommand) command,
                provider.parentCommandShort((ParentCommand) command, this.shortText),
                this.buildChildren((ParentCommand) command),
                buildArguments(provider, command));
        final List<AnnotatedCommand> commands = ((ParentCommand) command).getCommands();
        commands.forEach(
            childCommand ->
                AnnotatedCommandHelpTopic.helpMap.addTopic(
                    new AnnotatedCommandHelpTopic(
                        childCommand, (ParentCommand) command, provider)));
      } else {
        this.fullText =
            provider.commandFull(command, this.shortText, buildArguments(provider, command));
      }
    } else {
      this.name = provider.childCommandName(command, parent);
      this.shortText = provider.childCommandShort(command, parent);
      this.fullText =
          provider.childCommandFull(
              command, parent, this.shortText, buildArguments(provider, command));
    }
  }

  /**
   * Build the arguments help
   *
   * @param provider the messages provider to build the help topic
   * @param command the command to build the arguments from
   * @return the arguments built help
   */
  @NonNull
  private static String buildArguments(
      @NonNull MessagesProvider provider, @NonNull AnnotatedCommand command) {
    StringBuilder builder = Strings.getBuilder();
    command.getArguments().stream()
        .filter(argument -> argument instanceof Argument)
        .forEach(
            argument -> {
              if (((Argument<?>) argument).isRequired()) {
                builder.append(provider.requiredArgumentHelp((Argument<?>) argument));
              } else {
                builder.append(provider.optionalArgumentHelp((Argument<?>) argument));
              }
            });
    return builder.toString();
  }

  /**
   * Build the children help
   *
   * @param command the parent to get the children help from
   * @return the help as string
   */
  @NonNull
  private String buildChildren(@NonNull ParentCommand command) {
    StringBuilder builder = Strings.getBuilder();
    final List<AnnotatedCommand> commands = command.getCommands();
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

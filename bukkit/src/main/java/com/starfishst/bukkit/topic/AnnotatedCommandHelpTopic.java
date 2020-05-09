package com.starfishst.bukkit.topic;

import com.starfishst.bukkit.AnnotatedCommand;
import com.starfishst.bukkit.ParentCommand;
import com.starfishst.bukkit.utils.BukkitUtils;
import com.starfishst.core.arguments.Argument;
import com.starfishst.core.utils.Strings;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** The {@link HelpTopic} for the commands generated with annotations */
class AnnotatedCommandHelpTopic extends HelpTopic {

  /** The server to get the {@link HelpMap} from */
  @NotNull private static final Server server = Bukkit.getServer();
  /** The HelpMap to register the command */
  @NotNull private static final HelpMap helpMap = AnnotatedCommandHelpTopic.server.getHelpMap();

  /** The template for commands */
  @NotNull private static final String TEMPLATE = "&7{0} \n &7Usage: {1} \n &7Arguments: \n {2}";
  /** The template for {@link ParentCommand} */
  @NotNull
  private static final String PARENT =
      "&7{0} \n &7Usage: {1} \n &7Subcommands (use /help {2}.<child>): \n {3} &7Arguments: \n {4}";

  /** The template for {@link com.starfishst.core.annotations.Optional} arguments */
  @NotNull private static final String OPTIONAL = "&e(&6{0}&e)&7: &7{1}";
  /** The template for {@link com.starfishst.core.annotations.Required} arguments */
  @NotNull private static final String REQUIRED = "&e<&6{0}&e>&7: &7{1}";
  /** The template for children commands */
  @NotNull private static final String CHILD = "&e{0}&7: {1}";

  /**
   * Create the topic
   *
   * @param command the command to create the topic from
   * @param parent the parent if the command has one
   */
  AnnotatedCommandHelpTopic(@NotNull AnnotatedCommand command, @Nullable AnnotatedCommand parent) {
    final String permission = command.getPermission();
    this.amendedPermission = permission == null ? null : permission.isEmpty() ? null : permission;
    if (parent == null) {
      this.shortText = BukkitUtils.color(command.getDescription());
      this.name = "/" + command.getName();
      if (command instanceof ParentCommand) {
        this.fullText =
            BukkitUtils.getMessage(
                AnnotatedCommandHelpTopic.PARENT,
                "(Parent) " + this.shortText,
                command.getUsage(),
                command.getName(),
                this.buildChildren(((ParentCommand) command)),
                AnnotatedCommandHelpTopic.buildArguments(command));
        final List<AnnotatedCommand> commands = ((ParentCommand) command).getCommands();
        commands.forEach(
            childCommand ->
                AnnotatedCommandHelpTopic.helpMap.addTopic(
                    new AnnotatedCommandHelpTopic(childCommand, command)));
      } else {
        this.fullText =
            BukkitUtils.getMessage(
                AnnotatedCommandHelpTopic.TEMPLATE,
                this.shortText,
                command.getUsage(),
                AnnotatedCommandHelpTopic.buildArguments(command));
      }
    } else {
      this.name = "/" + parent.getName() + "." + command.getName();
      this.shortText = BukkitUtils.color(command.getDescription());
      this.fullText =
          BukkitUtils.getMessage(
              AnnotatedCommandHelpTopic.TEMPLATE,
              this.shortText,
              command.getUsage(),
              AnnotatedCommandHelpTopic.buildArguments(command));
    }
  }

  /**
   * Build the arguments help
   *
   * @param command the command to build the arguments from
   * @return the arguments built help
   */
  @NotNull
  private static String buildArguments(@NotNull AnnotatedCommand command) {
    StringBuilder builder = Strings.getBuilder();
    command.getArguments().stream()
        .filter(argument -> argument instanceof Argument)
        .forEach(
            argument -> {
              final String description = ((Argument<?>) argument).getDescription();
              if (((Argument<?>) argument).isRequired()) {
                builder.append(
                    Strings.buildMessage(
                        AnnotatedCommandHelpTopic.REQUIRED,
                        ((Argument<?>) argument).getName(),
                        description));
              } else {
                builder.append(
                    Strings.buildMessage(
                        AnnotatedCommandHelpTopic.OPTIONAL,
                        ((Argument<?>) argument).getName(),
                        description));
              }
              builder.append("\n");
            });
    return builder.toString();
  }

  /**
   * Build the children help
   *
   * @param command the parent to get the children help from
   * @return the help as string
   */
  @NotNull
  private String buildChildren(@NotNull ParentCommand command) {
    StringBuilder builder = Strings.getBuilder();
    final List<AnnotatedCommand> commands = command.getCommands();
    commands.forEach(
        annotatedCommand -> {
          builder.append(
              Strings.buildMessage(
                  AnnotatedCommandHelpTopic.CHILD,
                  annotatedCommand.getName(),
                  annotatedCommand.getDescription()));
          builder.append("\n");
        });
    return builder.toString();
  }

  @Override
  public boolean canSee(@NotNull CommandSender commandSender) {
    if (this.amendedPermission == null) {
      return true;
    } else {
      return commandSender.hasPermission(this.amendedPermission);
    }
  }
}

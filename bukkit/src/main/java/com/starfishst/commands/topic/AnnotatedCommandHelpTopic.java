package com.starfishst.commands.topic;

import com.starfishst.commands.AnnotatedCommand;
import com.starfishst.commands.ParentCommand;
import com.starfishst.commands.utils.BukkitUtils;
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

class AnnotatedCommandHelpTopic extends HelpTopic {

  @NotNull private static final Server server = Bukkit.getServer();
  @NotNull private static final HelpMap helpMap = AnnotatedCommandHelpTopic.server.getHelpMap();
  @NotNull
  private static final String TEMPLATE = "&7{0} \n &7Usage: {1} \n &7Arguments: \n {2}",
      PARENT =
          "&7{0} \n &7Usage: {1} \n &7Subcommands (use /help {2}.<child>): \n {3} &7Arguments: \n {4}",
      OPTIONAL = "&e(&6{0}&e)&7: &7{1}",
      REQUIRED = "&e<&6{0}&e>&7: &7{1}",
      CHILD = "&e{0}&7: {1}";

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

  @NotNull
  private static String buildArguments(@NotNull AnnotatedCommand command) {
    StringBuilder builder = Strings.getBuilder();
    command.getArguments().stream()
        .filter(argument -> argument instanceof Argument)
        .forEach(
            argument -> {
              final String description = ((Argument) argument).getDescription();
              if (((Argument) argument).isRequired()) {
                builder.append(
                    Strings.buildMessage(
                        AnnotatedCommandHelpTopic.REQUIRED,
                        ((Argument) argument).getName(),
                        description));
              } else {
                builder.append(
                    Strings.buildMessage(
                        AnnotatedCommandHelpTopic.OPTIONAL,
                        ((Argument) argument).getName(),
                        description));
              }
              builder.append("\n");
            });
    return builder.toString();
  }

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

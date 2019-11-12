package com.starfishst.commands.topic;

import com.starfishst.commands.AnnotatedCommand;
import com.starfishst.commands.ParentCommand;
import com.starfishst.commands.objects.Argument;
import com.starfishst.commands.objects.CommandContext;
import com.starfishst.commands.utils.Strings;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class AnnotatedCommandHelpTopic extends HelpTopic {

  @NotNull
  private static final Server server = Bukkit.getServer();
  @NotNull
  private static final HelpMap helpMap = server.getHelpMap();
  @NotNull
  private static final String TEMPLATE = "&7{0} \n &7Usage: {1} \n &7Arguments: \n",
      PARENT =
          "&7{0} \n &7Usage: {1} \n &7Subcommands (use /help {2}.<child>): \n {3} &7Arguments: \n {4}",
      OPTIONAL = "&e(&6{0}&e)&7: &7{1}",
      REQUIRED = "&e<&6{0}&e>&7: &7{1}",
      CHILD = "&e{0}&7: {1}";

  AnnotatedCommandHelpTopic(@NotNull AnnotatedCommand command, @Nullable AnnotatedCommand parent) {
    final String permission = command.getPermission();
    this.amendedPermission = permission == null ? null : permission.isEmpty() ? null : permission;
    if (parent == null) {
      this.shortText = Strings.color(command.getDescription());
      this.name = "/" + command.getName();
      if (command instanceof ParentCommand) {
        this.fullText =
            Strings.getMessage(
                PARENT,
                "(Parent) " + shortText,
                command.getUsage(),
                command.getName(),
                buildChildren(((ParentCommand) command)),
                buildArguments(command));

        final List<AnnotatedCommand> commands = ((ParentCommand) command).getCommands();
        commands.forEach(
            childCommand -> helpMap.addTopic(new AnnotatedCommandHelpTopic(childCommand, command)));
      } else {
        this.fullText =
            Strings.getMessage(TEMPLATE, shortText, command.getUsage(), buildArguments(command));
      }
    } else {
      this.name = "/" + parent.getName() + "." + command.getName();
      this.shortText = Strings.color(command.getDescription());
      this.fullText =
          Strings.getMessage(TEMPLATE, shortText, command.getUsage(), buildArguments(command));
    }
  }

  @NotNull
  private static String buildArguments(@NotNull AnnotatedCommand command) {
    StringBuilder builder = Strings.getBuilder();
    for (Argument argument : command.getArguments()) {
      final Class<?> clazz = argument.getClazz();
      if (!clazz.isAssignableFrom(CommandContext.class)) {
        final String description = argument.getDescription();
        if (argument.isRequired()) {
          builder.append(Strings.getMessage(REQUIRED, argument.getName(), description));
        } else {
          builder.append(Strings.getMessage(OPTIONAL, argument.getName(), description));
        }
        builder.append("\n");
      }
    }
    return builder.toString();
  }

  @NotNull
  private String buildChildren(@NotNull ParentCommand command) {
    StringBuilder builder = Strings.getBuilder();
    final List<AnnotatedCommand> commands = command.getCommands();
    commands.forEach(
        annotatedCommand -> {
          builder.append(
              Strings.getMessage(
                  CHILD, annotatedCommand.getName(), annotatedCommand.getDescription()));
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

package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.objects.Argument;
import com.starfishst.commands.objects.Result;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParentCommand extends AnnotatedCommand {

  @NotNull private final List<AnnotatedCommand> commands = new ArrayList<>();
  @NotNull private final List<String> commandsAlias = new ArrayList<>();

  ParentCommand(
      @NotNull final Object clazz,
      @NotNull final Method method,
      final Command command,
      @NotNull final Argument[] arguments) {
    super(clazz, method, command, arguments);
  }

  @Override
  public Result execute(@NotNull final CommandSender sender, @NotNull final String[] strings) {
    if (strings.length >= 1) {
      final AnnotatedCommand command = this.getCommand(strings[0]);
      if (command != null) {
        return command.execute(sender, Arrays.copyOfRange(strings, 1, strings.length));
      }
    }
    return super.execute(sender, strings);
  }

  @NotNull
  @Override
  public List<String> tabComplete(
      @NotNull final CommandSender sender, @NotNull final String alias, final String[] strings)
      throws IllegalArgumentException {
    if (strings.length == 1) {
      return this.commandsAlias;
    } else if (strings.length >= 2) {
      final AnnotatedCommand command = this.getCommand(strings[0]);
      if (command != null) {
        return command.tabComplete(sender, alias, Arrays.copyOfRange(strings, 1, strings.length));
      } else {
        return super.tabComplete(sender, alias, Arrays.copyOfRange(strings, 1, strings.length));
      }
    } else {
      return new ArrayList<>();
    }
  }

  @Nullable
  private AnnotatedCommand getCommand(@NotNull final String name) {
    for (final AnnotatedCommand command : this.commands) {
      final String commandName = command.getName();
      if (commandName.equals(name)) {
        return command;
      }
      for (final String alias : command.getAliases()) {
        if (alias.equalsIgnoreCase(name)) {
          return command;
        }
      }
    }
    return null;
  }

  void add(@NotNull final AnnotatedCommand cmd) {
    this.commands.add(cmd);
    this.commandsAlias.add(cmd.getName());
  }

  @NotNull
  public List<AnnotatedCommand> getCommands() {
    return commands;
  }
}

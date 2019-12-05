package com.starfishst.commands;

import com.starfishst.commands.annotations.Command;
import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.result.Result;
import com.starfishst.core.IParentCommand;
import com.starfishst.core.arguments.type.ISimpleArgument;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParentCommand extends AnnotatedCommand
    implements IParentCommand<AnnotatedCommand, CommandContext> {

  @NotNull private final CommandManagerOptions options;
  @NotNull private final List<AnnotatedCommand> commands = new ArrayList<>();
  @NotNull private final List<String> commandsAlias = new ArrayList<>();

  ParentCommand(
      @NotNull Object clazz,
      @NotNull Method method,
      @NotNull List<ISimpleArgument> arguments,
      @NotNull Command command,
      CommandManagerOptions options) {
    super(clazz, method, arguments, command);
    this.options = options;
  }

  @Override
  public @NotNull List<AnnotatedCommand> getCommands() {
    return this.commands;
  }

  @Override
  public @Nullable AnnotatedCommand getCommand(@NotNull String name) {
    return this.commands.stream()
        .filter(
            annotatedCommand ->
                annotatedCommand.getName().equalsIgnoreCase(name)
                    || annotatedCommand.getAliases().stream()
                            .filter(alias -> alias.equalsIgnoreCase(name))
                            .findFirst()
                            .orElse(null)
                        != null)
        .findFirst()
        .orElse(null);
  }

  @Override
  public void addCommand(@NotNull AnnotatedCommand command) {
    this.commands.add(command);
    this.commandsAlias.add(command.getName());

    if (this.options.isIncludeAliases()) {
      this.commandsAlias.addAll(command.getAliases());
    }
  }

  @Override
  public @NotNull Result execute(@NotNull CommandContext context) {
    String[] strings = context.getStrings();
    if (strings.length >= 1) {
      AnnotatedCommand command = this.getCommand(strings[0]);
      if (command != null) {
        return command.execute(
            new CommandContext(
                context.getSender(), Arrays.copyOfRange(strings, 1, strings.length)));
      } else {
        return super.execute(context);
      }
    } else {
      return super.execute(context);
    }
  }

  @NotNull
  @Override
  public List<String> tabComplete(
      @NotNull final CommandSender sender,
      @NotNull final String alias,
      @NotNull final String[] strings)
      throws IllegalArgumentException {
    if (strings.length == 1) {
      return StringUtil.copyPartialMatches(
          strings[strings.length - 1], this.commandsAlias, new ArrayList<>());
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
}

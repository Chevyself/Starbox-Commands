package com.starfishst.bungee;

import com.starfishst.bungee.annotations.Command;
import com.starfishst.bungee.messages.MessagesProvider;
import com.starfishst.core.IParentCommand;
import com.starfishst.core.arguments.type.ISimpleArgument;
import com.starfishst.core.utils.Strings;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Parent command for bungee commands */
public class ParentCommand extends AnnotatedCommand implements IParentCommand<AnnotatedCommand> {

  /** The list of children commands */
  @NotNull private final List<AnnotatedCommand> commands = new ArrayList<>();
  /** A list of the names of the commands registered in this parent */
  @NotNull private final List<String> commandAliases = new ArrayList<>();

  /**
   * Create an instance
   *
   * @param object the object that contains the method
   * @param method the method to invoke
   * @param arguments the arguments to get the parameters for the method to invoke
   * @param command the annotations to get the parameters of the command
   * @param messagesProvider the messages provider
   * @param plugin the plugin where this command was registered
   * @param async whether the command should be executed async
   */
  public ParentCommand(
      @NotNull Object object,
      @NotNull Method method,
      @NotNull List<ISimpleArgument<?>> arguments,
      @NotNull Command command,
      @NotNull MessagesProvider messagesProvider,
      @NotNull Plugin plugin,
      boolean async) {
    super(object, method, arguments, command, messagesProvider, plugin, async);
  }

  @Override
  public @NotNull List<AnnotatedCommand> getCommands() {
    return commands;
  }

  @Override
  public void execute(CommandSender sender, String[] strings) {
    if (strings.length >= 1) {
      AnnotatedCommand command = this.getCommand(strings[0]);
      if (command != null) {
        command.execute(sender, Arrays.copyOfRange(strings, 1, strings.length));
      } else {
        super.execute(sender, strings);
      }
    } else {
      super.execute(sender, strings);
    }
  }

  @Override
  public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
    if (strings.length == 1) {
      return Strings.copyPartials(strings[strings.length - 1], this.commandAliases);
    } else if (strings.length >= 2) {
      AnnotatedCommand command = this.getCommand(strings[0]);
      if (command != null) {
        return command.onTabComplete(commandSender, Arrays.copyOfRange(strings, 1, strings.length));
      } else {
        return super.onTabComplete(commandSender, Arrays.copyOfRange(strings, 1, strings.length));
      }
    } else {
      return new ArrayList<>();
    }
  }

  @Nullable
  @Override
  public AnnotatedCommand getCommand(@NotNull String name) {
    return this.commands.stream()
        .filter(
            command -> {
              if (command.getName().equalsIgnoreCase(name)) {
                return true;
              } else {
                for (String alias : command.getAliases()) {
                  if (alias.equalsIgnoreCase(name)) {
                    return true;
                  }
                }
              }
              return false;
            })
        .findFirst()
        .orElse(null);
  }

  @Override
  public void addCommand(@NotNull AnnotatedCommand command) {
    this.commands.add(command);
    this.commandAliases.add(command.getName());
  }
}

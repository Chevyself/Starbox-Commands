package me.googas.commands.bungee;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.IParentCommand;
import me.googas.commands.arguments.ISimpleArgument;
import me.googas.commands.bungee.annotations.Command;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.messages.MessagesProvider;
import me.googas.commands.objects.CommandSettings;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.starbox.Strings;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

/** Parent command for bungee commands */
public class ParentCommand extends AnnotatedCommand implements IParentCommand<AnnotatedCommand> {

  /** The list of children commands */
  @NonNull private final List<AnnotatedCommand> commands = new ArrayList<>();
  /** A list of the names of the commands registered in this parent */
  @NonNull @Getter private final List<String> commandAliases = new ArrayList<>();

  /**
   * Create an instance
   *
   * @param object the object that contains the method
   * @param method the method to invoke
   * @param arguments the arguments to get the parameters for the method to invoke
   * @param command the annotations to get the parameters of the command
   * @param messagesProvider the messages provider
   * @param plugin the plugin where this command was registered
   * @param registry the registry for the commands
   * @param settings the settings of the command
   */
  public ParentCommand(
      @NonNull Object object,
      @NonNull Method method,
      @NonNull List<ISimpleArgument<?>> arguments,
      @NonNull Command command,
      @NonNull MessagesProvider messagesProvider,
      @NonNull Plugin plugin,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull CommandSettings settings) {
    super(object, method, arguments, command, messagesProvider, plugin, registry, settings);
  }

  @Override
  public @NonNull List<AnnotatedCommand> getCommands() {
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

  @Override
  public AnnotatedCommand getCommand(@NonNull String name) {
    for (AnnotatedCommand command : this.commands) {
      if (command.getName().equalsIgnoreCase(name)) return command;
      for (String alias : command.getAliases()) {
        if (alias.equalsIgnoreCase(name)) return command;
      }
    }
    return null;
  }

  @Override
  public void addCommand(@NonNull AnnotatedCommand command) {
    this.commands.add(command);
    this.commandAliases.add(command.getName());
  }
}

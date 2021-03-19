package me.googas.commands.bukkit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.EasyCommandManager;
import me.googas.commands.ReflectCommand;
import me.googas.commands.annotations.Parent;
import me.googas.commands.arguments.Argument;
import me.googas.commands.bukkit.annotations.Command;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.messages.MessagesProvider;
import me.googas.commands.bukkit.result.Result;
import me.googas.commands.bukkit.topic.PluginHelpTopic;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.commands.exceptions.CommandRegistrationException;
import me.googas.commands.providers.registry.ProvidersRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.help.HelpMap;
import org.bukkit.plugin.Plugin;

/** TODO documentation */
public class CommandManager implements EasyCommandManager<CommandContext, BukkitCommand> {

  /** The bukkit help map */
  @NonNull private static final HelpMap helpMap = Bukkit.getServer().getHelpMap();
  /** The bukkit command map */
  @NonNull private static final CommandMap commandMap;

  static {
    try {
      commandMap = BukkitUtils.getCommandMap();
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new CommandRegistrationException("Command Map could not be accessed");
    }
  }

  @NonNull @Getter private final Plugin plugin;
  @NonNull @Getter private final ProvidersRegistry<CommandContext> registry;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter private final List<BukkitCommand> commands;

  public CommandManager(
      @NonNull Plugin plugin,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull MessagesProvider messagesProvider,
      @NonNull List<BukkitCommand> commands) {
    this.plugin = plugin;
    this.registry = registry;
    this.messagesProvider = messagesProvider;
    this.commands = commands;
  }

  /**
   * Registers the plugin used for the {@link CommandManager} into the {@link HelpMap} (/help) do
   * this after you've registered all your commands so they can be shown
   */
  public void registerPlugin() {
    CommandManager.helpMap.addTopic(new PluginHelpTopic(this.plugin, this, this.messagesProvider));
  }

  @Override
  public void register(@NonNull BukkitCommand command) {
    commandMap.register(this.plugin.getName(), command);
    this.commands.add(command);
  }

  @Override
  public @NonNull Collection<ReflectCommand<CommandContext>> parseCommands(@NonNull Object object) {
    List<ReflectCommand<CommandContext>> commands = new ArrayList<>();
    AnnotatedParentCommand parent = null;
    final Class<?> clazz = object.getClass();
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
        parent = (AnnotatedParentCommand) this.parseCommand(object, method);
        commands.add(parent);
        break;
      }
    }
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Command.class)) {
        final AnnotatedCommand cmd = this.parseCommand(object, method);
        if (parent != null) {
          parent.addCommand(cmd);
        } else {
          commands.add(cmd);
        }
      }
    }
    return commands;
  }

  @Override
  public @NonNull AnnotatedCommand parseCommand(@NonNull Object object, @NonNull Method method) {
    if (method.getReturnType() != Result.class || !method.getReturnType().equals(Void.TYPE))
      throw new IllegalArgumentException(method + " must return void or " + Result.class);
    if (!method.isAnnotationPresent(Command.class))
      throw new IllegalArgumentException(method + " is not annotated with " + Command.class);
    Command command = method.getAnnotation(Command.class);
    if (method.isAnnotationPresent(Parent.class)) {
      return new AnnotatedParentCommand(
          command,
          method,
          object,
          Argument.parseArguments(method.getParameterTypes(), method.getParameterAnnotations()),
          this,
          new ArrayList<>());
    } else {
      return new AnnotatedCommand(
          command,
          method,
          object,
          Argument.parseArguments(method.getParameterTypes(), method.getParameterAnnotations()),
          this);
    }
  }
}

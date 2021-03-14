package me.googas.commands.bukkit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.annotations.Nullable;
import me.googas.commands.ICommandManager;
import me.googas.commands.annotations.Parent;
import me.googas.commands.bukkit.annotations.Command;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.messages.MessagesProvider;
import me.googas.commands.bukkit.result.Result;
import me.googas.commands.bukkit.topic.AnnotatedCommandHelpTopicFactory;
import me.googas.commands.bukkit.topic.PluginHelpTopic;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.commands.exceptions.CommandRegistrationException;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.starbox.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.help.HelpMap;
import org.bukkit.plugin.Plugin;

/** The command manager for bukkit commands */
public class CommandManager implements ICommandManager<AnnotatedCommand> {

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

  /** The plugin that is using the command manager */
  @NonNull @Getter private final Plugin plugin;
  /** The provider for messages */
  @NonNull @Getter private final MessagesProvider messagesProvider;
  /** The list of commands that this manager handles */
  @NonNull @Getter private final List<AnnotatedCommand> commands = new ArrayList<>();
  /** The registry for commands */
  @NonNull @Getter private final ProvidersRegistry<CommandContext> registry;
  /** The options to use in the command manager */
  @NonNull @Getter @Setter private CommandManagerOptions options;

  /**
   * Create an instance
   *
   * @param plugin the plugin that will use the command manager
   * @param options the options for the command manager
   * @param messagesProvider the provider for messages
   * @param registry the registry for the command context
   */
  public CommandManager(
      @NonNull Plugin plugin,
      @NonNull CommandManagerOptions options,
      @NonNull MessagesProvider messagesProvider,
      @NonNull ProvidersRegistry<CommandContext> registry) {
    this.plugin = plugin;
    this.options = options;
    this.messagesProvider = messagesProvider;
    this.registry = registry;
    CommandManager.helpMap.registerHelpTopicFactory(
        AnnotatedCommand.class, new AnnotatedCommandHelpTopicFactory(messagesProvider));
  }

  /**
   * Registers the plugin used for the {@link CommandManager} into the {@link HelpMap} (/help) do
   * this after you've registered all your commands so they can be shown
   */
  public void registerPlugin() {
    CommandManager.helpMap.addTopic(new PluginHelpTopic(this.plugin, this, this.messagesProvider));
  }

  @Override
  public void registerCommand(@NonNull Object object) {
    Collection<AnnotatedCommand> commands = parseCommands(object);
    ParentCommand command = null;
    for (AnnotatedCommand annotatedCommand : commands) {
      if (annotatedCommand instanceof ParentCommand) {
        command = (ParentCommand) annotatedCommand;
        commandMap.register(this.plugin.getName(), annotatedCommand);
        this.commands.add(annotatedCommand);
      } else {
        if (command != null) {
          command.addCommand(annotatedCommand);
        } else {
          commandMap.register(plugin.getName(), annotatedCommand);
          this.commands.add(annotatedCommand);
        }
      }
    }
  }

  @Override
  public @NonNull Collection<AnnotatedCommand> parseCommands(@NonNull Object object) {
    List<AnnotatedCommand> commands = new ArrayList<>();
    ParentCommand parent = null;
    final Class<?> clazz = object.getClass();
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
        parent = (ParentCommand) this.parseCommand(object, method, true);
        commands.add(parent);
        break;
      }
    }
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Command.class)) {
        final AnnotatedCommand cmd = this.parseCommand(object, method, false);
        if (parent != null) {
          parent.addCommand(cmd);
        } else {
          commands.add(cmd);
        }
      }
    }
    return commands;
  }

  @Nullable
  @Override
  public ParentCommand getParent(@NonNull String alias) {
    for (AnnotatedCommand command : this.commands) {
      if (command instanceof ParentCommand) {
        if (command.getName().equalsIgnoreCase(alias)) {
          return (ParentCommand) command;
        }
        for (String commandAlias : command.getAliases()) {
          if (commandAlias.equalsIgnoreCase(alias)) {
            return (ParentCommand) command;
          }
        }
      }
    }
    return null;
  }

  @Override
  public void unregister() {
    for (AnnotatedCommand command : this.commands) {
      command.unregister(commandMap);
    }
    this.commands.clear();
  }

  @Override
  public @NonNull AnnotatedCommand parseCommand(
      @NonNull Object object, @NonNull Method method, boolean isParent) {
    if (method.getReturnType() == Result.class || method.getReturnType().equals(Void.TYPE)) {
      Annotation[][] annotations = method.getParameterAnnotations();
      Class<?>[] parameters = method.getParameterTypes();
      Command command = method.getAnnotation(Command.class);
      if (isParent) {
        return new ParentCommand(
            object,
            method,
            this.parseArguments(parameters, annotations),
            command,
            options,
            messagesProvider,
            plugin,
            registry,
            this.parseSettings(method));
      } else {
        return new AnnotatedCommand(
            object,
            method,
            this.parseArguments(parameters, annotations),
            command,
            messagesProvider,
            plugin,
            registry,
            this.parseSettings(method));
      }
    } else {
      throw new CommandRegistrationException(
          Strings.build("{0} must return {1} or void", method, Result.class));
    }
  }
}

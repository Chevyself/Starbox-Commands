package com.starfishst.commands.bukkit;

import com.starfishst.commands.bukkit.annotations.Command;
import com.starfishst.commands.bukkit.context.CommandContext;
import com.starfishst.commands.bukkit.messages.MessagesProvider;
import com.starfishst.commands.bukkit.result.Result;
import com.starfishst.commands.bukkit.topic.AnnotatedCommandHelpTopicFactory;
import com.starfishst.commands.bukkit.topic.PluginHelpTopic;
import com.starfishst.commands.bukkit.utils.BukkitUtils;
import com.starfishst.core.ICommandManager;
import com.starfishst.core.annotations.Parent;
import com.starfishst.core.exceptions.CommandRegistrationException;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.commons.Strings;
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
  @NonNull private final MessagesProvider messagesProvider;
  /** The list of commands that this manager handles */
  @NonNull @Getter private final List<AnnotatedCommand> commands = new ArrayList<>();
  /** The registry for commands */
  @NonNull private final ProvidersRegistry<CommandContext> registry;
  /** The options to use in the command manager */
  @NonNull @Getter @Setter private CommandManagerOptions options;
  /** The temporal parent to register commands there and not in the manager */
  private ParentCommand parent = null;

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

  /**
   * Get the registry of the manager
   *
   * @return the registry for the command context
   */
  @NonNull
  public ProvidersRegistry<CommandContext> getRegistry() {
    return registry;
  }

  @Override
  public void registerCommand(@NonNull Object object) {
    final Class<?> clazz = object.getClass();
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
        this.parent = (ParentCommand) this.parseCommand(object, method, true);
        CommandManager.commandMap.register(this.plugin.getName(), this.parent);
        commands.add(this.parent);
      }
    }
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Command.class) & !method.isAnnotationPresent(Parent.class)) {
        final AnnotatedCommand cmd = this.parseCommand(object, method, false);
        if (this.parent != null) {
          this.parent.addCommand(cmd);
        } else {
          CommandManager.commandMap.register(this.plugin.getName(), cmd);
          commands.add(cmd);
        }
      }
    }
    this.parent = null;
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

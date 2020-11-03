package com.starfishst.bukkit;

import com.starfishst.bukkit.annotations.Command;
import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.providers.CommandContextProvider;
import com.starfishst.bukkit.providers.CommandSenderArgumentProvider;
import com.starfishst.bukkit.providers.MaterialProvider;
import com.starfishst.bukkit.providers.OfflinePlayerProvider;
import com.starfishst.bukkit.providers.PlayerProvider;
import com.starfishst.bukkit.providers.PlayerSenderProvider;
import com.starfishst.bukkit.result.Result;
import com.starfishst.bukkit.topic.AnnotatedCommandHelpTopicFactory;
import com.starfishst.bukkit.topic.PluginHelpTopic;
import com.starfishst.bukkit.utils.BukkitUtils;
import com.starfishst.core.ICommandManager;
import com.starfishst.core.annotations.Parent;
import com.starfishst.core.exceptions.CommandRegistrationException;
import com.starfishst.core.providers.BooleanProvider;
import com.starfishst.core.providers.DoubleProvider;
import com.starfishst.core.providers.IntegerProvider;
import com.starfishst.core.providers.JoinedStringsProvider;
import com.starfishst.core.providers.LongProvider;
import com.starfishst.core.providers.StringProvider;
import com.starfishst.core.providers.TimeProvider;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.googas.commons.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.help.HelpMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** The command manager for bukkit commands */
public class CommandManager implements ICommandManager<AnnotatedCommand> {

  /** The bukkit help map */
  @NotNull private static final HelpMap helpMap = Bukkit.getServer().getHelpMap();
  /** The bukkit command map */
  @NotNull private static final CommandMap commandMap;

  static {
    try {
      commandMap = BukkitUtils.getCommandMap();
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new CommandRegistrationException("Command Map could not be accessed");
    }
  }

  /** The plugin that is using the command manager */
  @NotNull private final Plugin plugin;
  /** The options to use in the command manager */
  @NotNull private final CommandManagerOptions options;
  /** The provider for messages */
  @NotNull private final MessagesProvider messagesProvider;
  /** The temporal parent to register commands there and not in the manager */
  @Nullable private ParentCommand parent = null;
  /** The list of commands that this manager handles */
  @NotNull private final List<AnnotatedCommand> commands = new ArrayList<>();

  /** The registry for commands */
  @NotNull private final ProvidersRegistry<CommandContext> registry;

  /**
   * Create an instance
   *
   * @param plugin the plugin that will use the command manager
   * @param options the options for the command manager
   * @param messagesProvider the provider for messages
   * @param registry the registry for the command context
   */
  public CommandManager(
      @NotNull Plugin plugin,
      @NotNull CommandManagerOptions options,
      @NotNull MessagesProvider messagesProvider,
      @NotNull ProvidersRegistry<CommandContext> registry) {
    this.plugin = plugin;
    this.options = options;
    this.messagesProvider = messagesProvider;
    this.registry = registry;
    addProviders(registry, messagesProvider);
    CommandManager.helpMap.registerHelpTopicFactory(
        AnnotatedCommand.class, new AnnotatedCommandHelpTopicFactory(messagesProvider));
  }

  /**
   * Register the needed providers in the registry
   *
   * @param registry the registry of providers
   * @param messagesProvider the message provider
   */
  private void addProviders(
      @NotNull ProvidersRegistry<CommandContext> registry,
      @NotNull MessagesProvider messagesProvider) {
    registry.addProvider(new BooleanProvider<>(messagesProvider));
    registry.addProvider(new DoubleProvider<>(messagesProvider));
    registry.addProvider(new IntegerProvider<>(messagesProvider));
    registry.addProvider(new JoinedStringsProvider<>());
    registry.addProvider(new LongProvider<>(messagesProvider));
    registry.addProvider(new StringProvider<>());
    registry.addProvider(new TimeProvider<>(messagesProvider));
    registry.addProvider(new CommandContextProvider());
    registry.addProvider(new CommandSenderArgumentProvider());
    registry.addProvider(new MaterialProvider(messagesProvider));
    registry.addProvider(new OfflinePlayerProvider());
    registry.addProvider(new PlayerProvider(messagesProvider));
    registry.addProvider(new PlayerSenderProvider(messagesProvider));
  }

  /**
   * Registers the plugin used for the {@link CommandManager} into the {@link HelpMap} (/help) do
   * this after you've registered all your commands so they can be shown
   */
  public void registerPlugin() {
    CommandManager.helpMap.addTopic(new PluginHelpTopic(this.plugin, this, this.messagesProvider));
  }

  /**
   * Get the commands registered in the manager
   *
   * @return the commands registered in the manager
   */
  @NotNull
  public List<AnnotatedCommand> getCommands() {
    return commands;
  }

  /**
   * The options that uses the command manager
   *
   * @return the manager options
   */
  @NotNull
  public CommandManagerOptions getOptions() {
    return this.options;
  }

  @Override
  public void registerCommand(@NotNull Object object) {
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

  /**
   * Get if a command is async
   *
   * @param method the method of the command
   * @return true if the command is async
   */
  private boolean isAsync(@NotNull Method method) {
    HashMap<String, String> settings = parseSettings(method);
    return Boolean.parseBoolean(settings.getOrDefault("async", "false"));
  }

  /**
   * Get the registry of the manager
   *
   * @return the registry for the command context
   */
  @NotNull
  public ProvidersRegistry<CommandContext> getRegistry() {
    return registry;
  }

  @Override
  public @NotNull AnnotatedCommand parseCommand(
      @NotNull Object object, @NotNull Method method, boolean isParent) {
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
            isAsync(method),
            registry);
      } else {
        return new AnnotatedCommand(
            object,
            method,
            this.parseArguments(parameters, annotations),
            command,
            messagesProvider,
            plugin,
            isAsync(method),
            registry);
      }
    } else {
      throw new CommandRegistrationException(
          Strings.buildMessage("{0} must return {1} or void", method, Result.class));
    }
  }
}

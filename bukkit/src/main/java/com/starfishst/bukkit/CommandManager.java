package com.starfishst.bukkit;

import com.starfishst.bukkit.annotations.Command;
import com.starfishst.bukkit.context.CommandContext;
import com.starfishst.bukkit.messages.MessagesProvider;
import com.starfishst.bukkit.providers.CommandContextProvider;
import com.starfishst.bukkit.providers.CommandSenderArgumentProvider;
import com.starfishst.bukkit.providers.PlayerProvider;
import com.starfishst.bukkit.providers.PlayerSenderProvider;
import com.starfishst.bukkit.providers.registry.ImplProvidersRegistry;
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
import com.starfishst.core.providers.JoinedNumberProvider;
import com.starfishst.core.providers.JoinedStringsProvider;
import com.starfishst.core.providers.LongProvider;
import com.starfishst.core.providers.StringProvider;
import com.starfishst.core.providers.TimeProvider;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.help.HelpMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** The command manager for bukkit commands */
public class CommandManager implements ICommandManager<AnnotatedCommand> {

  /** The list of commands that this manager handles */
  @NotNull private static final List<AnnotatedCommand> commands = new ArrayList<>();
  /** The bukkit help map */
  @NotNull private static final HelpMap helpMap = Bukkit.getServer().getHelpMap();
  /** The bukkit command map */
  @NotNull private static final CommandMap commandMap;

  static {
    try {
      commandMap = BukkitUtils.getCommandMap();
      CommandManager.helpMap.registerHelpTopicFactory(
          AnnotatedCommand.class, new AnnotatedCommandHelpTopicFactory());
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

  /**
   * Create an instance
   *
   * @param plugin the plugin that will use the command manager
   * @param options the options for the command manager
   * @param messagesProvider the provider for messages
   */
  public CommandManager(
      @NotNull Plugin plugin,
      @NotNull CommandManagerOptions options,
      @NotNull MessagesProvider messagesProvider) {
    this.plugin = plugin;
    this.options = options;
    this.messagesProvider = messagesProvider;
    addProviders(ImplProvidersRegistry.getInstance(), messagesProvider);
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
    registry.addProvider(new PlayerProvider(messagesProvider));
    registry.addProvider(new PlayerSenderProvider(messagesProvider));
    registry.addProvider(new JoinedNumberProvider<>(messagesProvider));
  }

  /**
   * Registers the plugin used for the {@link CommandManager} into the {@link HelpMap} (/help) do
   * this after you've registered all your commands so they can be shown
   */
  public void registerPlugin() {
    CommandManager.helpMap.addTopic(new PluginHelpTopic(this.plugin));
  }

  /**
   * Get the commands registered in the manager
   *
   * @return the commands registered in the manager
   */
  @NotNull
  public static List<AnnotatedCommand> getCommands() {
    return CommandManager.commands;
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
        CommandManager.commands.add(this.parent);
      }
    }
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Command.class) & !method.isAnnotationPresent(Parent.class)) {
        final AnnotatedCommand cmd = this.parseCommand(object, method, false);
        if (this.parent != null) {
          this.parent.addCommand(cmd);
        } else {
          CommandManager.commandMap.register(this.plugin.getName(), cmd);
          CommandManager.commands.add(cmd);
        }
      }
    }
    this.parent = null;
  }

  @Override
  public @NotNull AnnotatedCommand parseCommand(
      @NotNull Object object, @NotNull Method method, boolean isParent) {
    if (method.getReturnType() == Result.class) {
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
            messagesProvider);
      } else {
        return new AnnotatedCommand(
            object,
            method,
            this.parseArguments(parameters, annotations),
            command,
            messagesProvider);
      }
    } else {
      throw new CommandRegistrationException("{0} must return {1}");
    }
  }
}

package me.googas.commands.bukkit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.StarboxCommandManager;
import me.googas.commands.annotations.Parent;
import me.googas.commands.arguments.Argument;
import me.googas.commands.bukkit.annotations.Command;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.messages.BukkitMessagesProvider;
import me.googas.commands.bukkit.messages.MessagesProvider;
import me.googas.commands.bukkit.result.Result;
import me.googas.commands.bukkit.topic.PluginHelpTopic;
import me.googas.commands.bukkit.utils.BukkitUtils;
import me.googas.commands.exceptions.CommandRegistrationException;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.providers.type.StarboxContextualProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.help.HelpMap;
import org.bukkit.plugin.Plugin;

/**
 * This manager is used for registering commands inside the {@link CommandMap} which makes them work
 * in any Bukkit server.
 *
 * <p>The easiest way to create commands is using reflection with the method {@link
 * #parseCommands(Object)} those parsed commands can be later registered in the {@link CommandMap}
 * using {@link #registerAll(Collection)}.
 *
 * <p>To create a {@link CommandManager} instance you simply need the {@link Plugin} which will be
 * related to the commands, a {@link ProvidersRegistry} you can use {@link
 * me.googas.commands.bukkit.providers.registry.BukkitProvidersRegistry} which includes some
 * providers that are intended for Bukkit use you can even extend it to add more in the constructor
 * or use {@link ProvidersRegistry#addProvider(StarboxContextualProvider)}, you also ned a {@link
 * MessagesProvider} which is mostly used to display error commands or create the {@link
 * org.bukkit.help.HelpTopic} for the commands registered in this manager to be added inside the
 * built-in bukkit command "/help" the default implementation is {@link BukkitMessagesProvider}:
 *
 * <pre>{@code
 * CommandManager manager =
 *         new CommandManager(
 *              this, new BukkitProvidersRegistry(), new BukkitMessagesProvider(), new ArrayList<>());
 *
 * }</pre>
 */
public class CommandManager implements StarboxCommandManager<CommandContext, StarboxBukkitCommand> {

  /**
   * The Bukkit HelpMap which is used to parseAndRegister. the {@link org.bukkit.help.HelpTopic} for
   * the {@link Plugin} using {@link #registerPlugin()} or all the topics for the {@link
   * StarboxBukkitCommand}
   */
  @NonNull private static final HelpMap helpMap = Bukkit.getServer().getHelpMap();
  /**
   * This is the {@link CommandMap} which contains all the registered commands. It is obtained using
   * reflection thru the method {@link BukkitUtils#getCommandMap()}
   */
  @NonNull private static final CommandMap commandMap;

  static {
    try {
      commandMap = BukkitUtils.getCommandMap();
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new CommandRegistrationException("CommandMap could not be accessed");
    }
  }

  @NonNull @Getter private final Plugin plugin;
  @NonNull @Getter private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter private final List<StarboxBukkitCommand> commands = new ArrayList<>();

  /**
   * Create an instance
   *
   * @param plugin the plugin that is related to the commands and other Bukkit actions such as
   *     creating tasks with the {@link org.bukkit.scheduler.BukkitScheduler}
   * @param providersRegistry the providers registry to provide the array of {@link Object} to
   *     invoke {@link AnnotatedCommand} using reflection or to be used in {@link CommandContext}
   * @param messagesProvider the messages provider for important messages and {@link
   *     org.bukkit.help.HelpTopic} of commands and the "plugin"
   */
  public CommandManager(
      @NonNull Plugin plugin,
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider messagesProvider) {
    this.plugin = plugin;
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
  }

  /**
   * Registers {@link #plugin} inside the {@link HelpMap} you can learn more about this in {@link
   * PluginHelpTopic} but basically this will make possible to do: "/help [plugin-name]"
   */
  public void registerPlugin() {
    CommandManager.helpMap.addTopic(new PluginHelpTopic(this.plugin, this, this.messagesProvider));
  }

  @NonNull
  @Override
  public CommandManager register(@NonNull StarboxBukkitCommand command) {
    CommandManager.commandMap.register(this.plugin.getName(), command);
    this.commands.add(command);
    return this;
  }

  @Override
  public @NonNull Collection<AnnotatedCommand> parseCommands(@NonNull Object object) {
    List<AnnotatedCommand> commands = new ArrayList<>();
    AnnotatedCommand parent = null;
    final Class<?> clazz = object.getClass();
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
        parent = this.parseCommand(object, method);
        commands.add(parent);
        break;
      }
    }
    for (final Method method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(Command.class) && !method.isAnnotationPresent(Parent.class)) {
        final AnnotatedCommand cmd = this.parseCommand(object, method);
        if (parent != null) {
          parent.addChildren(cmd);
        } else {
          commands.add(cmd);
        }
      }
    }
    return commands;
  }

  @Override
  public @NonNull AnnotatedCommand parseCommand(@NonNull Object object, @NonNull Method method) {
    if (!Result.class.isAssignableFrom(method.getReturnType())
        && !method.getReturnType().equals(Void.TYPE)) {
      throw new IllegalArgumentException(method + " must return void or " + Result.class);
    }
    if (!method.isAnnotationPresent(Command.class))
      throw new IllegalArgumentException(method + " is not annotated with " + Command.class);
    Command command = method.getAnnotation(Command.class);
    return new AnnotatedCommand(
        command,
        method,
        object,
        Argument.parseArguments(method.getParameterTypes(), method.getParameterAnnotations()),
        this,
        new ArrayList<>());
  }

  @Override
  public @NonNull CommandManager parseAndRegister(@NonNull Object object) {
    this.registerAll(this.parseCommands(object));
    return this;
  }

  @Override
  public @NonNull CommandManager parseAndRegisterAll(@NonNull Object... objects) {
    return (CommandManager) StarboxCommandManager.super.parseAndRegisterAll(objects);
  }

  @Override
  public @NonNull CommandManager registerAll(
      @NonNull Collection<? extends StarboxBukkitCommand> commands) {
    return (CommandManager) StarboxCommandManager.super.registerAll(commands);
  }

  @Override
  public @NonNull CommandManager registerAll(@NonNull StarboxBukkitCommand... commands) {
    return (CommandManager) StarboxCommandManager.super.registerAll(commands);
  }
}

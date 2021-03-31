package me.googas.commands.bungee;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.EasyCommandManager;
import me.googas.commands.annotations.Parent;
import me.googas.commands.arguments.Argument;
import me.googas.commands.bungee.annotations.Command;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.messages.BungeeMessagesProvider;
import me.googas.commands.bungee.messages.MessagesProvider;
import me.googas.commands.bungee.result.Result;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.providers.type.EasyContextualProvider;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

/**
 * This manager is used for registering commands inside the {@link PluginManager} which makes them
 * work in any Bungee server.
 *
 * <p>The easiest way to create commands is using reflection with the method {@link
 * #parseCommands(Object)} those parsed commands can be later registered in the {@link
 * PluginManager} using {@link #registerAll(Collection)}.
 *
 * <p>To create a {@link CommandManager} instance you simply need the {@link Plugin} which will be
 * related to the commands, a {@link ProvidersRegistry} you can use {@link
 * me.googas.commands.bungee.providers.registry.BungeeProvidersRegistry} which includes some
 * providers that are intended for Bungee use you can even extend it to add more in the constructor
 * or use {@link ProvidersRegistry#addProvider(EasyContextualProvider)}, you also ned a {@link
 * MessagesProvider} which is used to display error commands the commands the default implementation
 * is {@link BungeeMessagesProvider}.
 *
 * <pre>{@code
 * CommandManager manager =
 *         new CommandManager(
 *              this, new BungeeMessagesProvider(), new BungeeProvidersRegistry());
 *
 * }</pre>
 *
 * You can learn more about it in {@link BungeeCommand} which is the main class for the
 * easy-commands Bungee plugin.
 */
public class CommandManager implements EasyCommandManager<CommandContext, BungeeCommand> {

  @NonNull @Getter private final Plugin plugin;
  @NonNull @Getter private final PluginManager manager;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull @Getter private final List<BungeeCommand> commands = new ArrayList<>();

  /**
   * Create an instance
   *
   * @param plugin the plugin that is related to the commands and other Bungee actions such as
   *     creating tasks with the {@link net.md_5.bungee.api.scheduler.TaskScheduler}
   * @param messagesProvider the messages provider for important messages
   * @param providersRegistry the providers registry to provide the array of {@link Object} to
   *     invoke {@link AnnotatedCommand} using reflection or to be used in {@link CommandContext}
   */
  public CommandManager(
      @NonNull Plugin plugin,
      @NonNull MessagesProvider messagesProvider,
      @NonNull ProvidersRegistry<CommandContext> providersRegistry) {
    this.plugin = plugin;
    this.manager = plugin.getProxy().getPluginManager();
    this.messagesProvider = messagesProvider;
    this.providersRegistry = providersRegistry;
  }

  @Override
  public @NonNull CommandManager register(@NonNull BungeeCommand command) {
    this.manager.registerCommand(this.plugin, command);
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
      if (!method.isAnnotationPresent(Parent.class) && method.isAnnotationPresent(Command.class)) {
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
    Command command = method.getAnnotation(Command.class);
    return new AnnotatedCommand(
        command, new ArrayList<>(), this, object, method, Argument.parseArguments(method));
  }
}

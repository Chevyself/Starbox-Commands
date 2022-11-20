package chevyself.github.commands.bungee;

import chevyself.github.commands.Middleware;
import chevyself.github.commands.StarboxCommandManager;
import chevyself.github.commands.annotations.Parent;
import chevyself.github.commands.arguments.Argument;
import chevyself.github.commands.bungee.annotations.Command;
import chevyself.github.commands.bungee.context.CommandContext;
import chevyself.github.commands.bungee.messages.BungeeMessagesProvider;
import chevyself.github.commands.bungee.messages.MessagesProvider;
import chevyself.github.commands.bungee.middleware.CooldownMiddleware;
import chevyself.github.commands.bungee.middleware.PermissionMiddleware;
import chevyself.github.commands.bungee.middleware.ResultHandlingMiddleware;
import chevyself.github.commands.bungee.providers.registry.BungeeProvidersRegistry;
import chevyself.github.commands.bungee.result.BungeeResult;
import chevyself.github.commands.flags.Option;
import chevyself.github.commands.providers.registry.ProvidersRegistry;
import chevyself.github.commands.providers.type.StarboxContextualProvider;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
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
 * related to the commands, a {@link ProvidersRegistry} you can use {@link BungeeProvidersRegistry}
 * which includes some providers that are intended for Bungee use you can even extend it to add more
 * in the constructor or use {@link ProvidersRegistry#addProvider(StarboxContextualProvider)}, you
 * also need a {@link MessagesProvider} which is used to display error commands the default
 * implementation is {@link BungeeMessagesProvider}.
 *
 * <pre>{@code
 * CommandManager manager =
 *         new CommandManager(
 *              this, new BungeeMessagesProvider(), new BungeeProvidersRegistry());
 *
 * }</pre>
 */
public class CommandManager implements StarboxCommandManager<CommandContext, BungeeCommand> {

  @NonNull @Getter private final Plugin plugin;
  @NonNull @Getter private final PluginManager manager;
  @NonNull @Getter private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull @Getter private final MessagesProvider messagesProvider;

  @NonNull @Getter
  private final List<Middleware<CommandContext>> globalMiddlewares = new ArrayList<>();

  @NonNull @Getter private final List<Middleware<CommandContext>> middlewares = new ArrayList<>();
  @NonNull @Getter private final List<BungeeCommand> commands = new ArrayList<>();

  /**
   * Create an instance.
   *
   * @param plugin the plugin that is related to the commands and other Bungee actions such as
   *     creating tasks with the {@link net.md_5.bungee.api.scheduler.TaskScheduler}
   * @param providersRegistry the providers' registry to provide the array of {@link Object} to
   *     invoke {@link AnnotatedCommand} using reflection or to be used in {@link CommandContext}
   * @param messagesProvider the messages provider for important messages
   */
  public CommandManager(
      @NonNull Plugin plugin,
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider messagesProvider) {
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

  /**
   * Adds the default middlewares.
   *
   * <p>The default middlewares are:
   *
   * <ul>
   *   <li>{@link CooldownMiddleware}
   *   <li>{@link PermissionMiddleware}
   *   <li>{@link ResultHandlingMiddleware}
   * </ul>
   *
   * @return this same instance
   */
  @NonNull
  public CommandManager addDefaultMiddlewares() {
    this.addGlobalMiddlewares(
        new CooldownMiddleware(), new PermissionMiddleware(), new ResultHandlingMiddleware());
    return this;
  }

  @Override
  public @NonNull AnnotatedCommand parseCommand(@NonNull Object object, @NonNull Method method) {
    if (!BungeeResult.class.isAssignableFrom(method.getReturnType())
        && !method.getReturnType().equals(Void.TYPE)) {
      throw new IllegalArgumentException(method + " must return void or " + BungeeResult.class);
    }
    Command command = method.getAnnotation(Command.class);
    List<Argument<?>> arguments = Argument.parseArguments(method);
    return new AnnotatedCommand(
        this,
        plugin,
        command.aliases()[0],
        command.permission().isEmpty() ? null : command.permission(),
        Option.of(command.options()),
        this.getMiddlewares(command),
        command.async(),
        CooldownManager.of(command.cooldown()).orElse(null),
        method,
        object,
        arguments,
        new ArrayList<>(),
        Arrays.copyOfRange(command.aliases(), 1, command.aliases().length));
  }

  @NonNull
  private List<Middleware<CommandContext>> getMiddlewares(@NonNull Command command) {
    return StarboxCommandManager.getMiddlewares(
        this.getGlobalMiddlewares(), this.getMiddlewares(), command.include(), command.exclude());
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
      @NonNull Collection<? extends BungeeCommand> commands) {
    return (CommandManager) StarboxCommandManager.super.registerAll(commands);
  }

  @Override
  public @NonNull CommandManager registerAll(@NonNull BungeeCommand... commands) {
    return (CommandManager) StarboxCommandManager.super.registerAll(commands);
  }

  @Override
  public void close() {
    this.commands.forEach(manager::unregisterCommand);
    this.commands.clear();
  }

  @SafeVarargs
  @Override
  public final @NonNull CommandManager addGlobalMiddlewares(
      @NonNull Middleware<CommandContext>... middlewares) {
    StarboxCommandManager.super.addGlobalMiddlewares(middlewares);
    return this;
  }

  @SafeVarargs
  @Override
  public final @NonNull CommandManager addMiddlewares(
      @NonNull Middleware<CommandContext>... middlewares) {
    StarboxCommandManager.super.addMiddlewares(middlewares);
    return this;
  }

  @Override
  public @NonNull CommandManager addGlobalMiddleware(
      @NonNull Middleware<CommandContext> middleware) {
    this.globalMiddlewares.add(middleware);
    return this;
  }

  @Override
  public @NonNull CommandManager addMiddleware(@NonNull Middleware<CommandContext> middleware) {
    this.middlewares.add(middleware);
    return this;
  }
}

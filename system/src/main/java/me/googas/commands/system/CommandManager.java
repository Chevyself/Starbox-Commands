package me.googas.commands.system;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.Middleware;
import me.googas.commands.StarboxCommandManager;
import me.googas.commands.annotations.Parent;
import me.googas.commands.arguments.Argument;
import me.googas.commands.flags.Option;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.system.context.CommandContext;
import me.googas.commands.time.Time;

/**
 * This manager is used for registering commands inside the {@link #getCommands()}. Then it is
 * provided to the {@link CommandListener} to get the commands and execute them
 *
 * @see CommandListener
 *     <p>The easiest way to create commands is using reflection with the method {@link
 *     #parseCommands(Object)} those parsed commands can be later registered in the {@link
 *     #getCommands()} using {@link #registerAll(Collection)}.
 *     <p>To create a {@link CommandManager} instance you simply need the {@link String} prefix
 *     which will diferentiate the commmands from normal messages
 *     <pre>{@code
 * SystemMessagesProvider messages = new SystemMessagesProvider();
 * CommandManager manager =
 *         new CommandManager(
 *              ":", new ProvidersRegistry<>(messages), messages);
 *
 * }</pre>
 */
public class CommandManager implements StarboxCommandManager<CommandContext, SystemCommand> {

  @NonNull @Getter private final List<SystemCommand> commands = new ArrayList<>();
  @NonNull @Getter private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter private final List<Middleware<CommandContext>> globalMiddlewares;
  @NonNull @Getter private final List<Middleware<CommandContext>> middlewares;
  @NonNull @Getter private final CommandListener listener;

  /**
   * Create the command manager.
   *
   * @param prefix the prefix that will differentiate commands from other types of messages
   * @param providersRegistry the providers' registry to provide the array of {@link Object} to
   *     invoke {@link ReflectSystemCommand} using reflection or to be used in {@link
   *     CommandContext}
   * @param messagesProvider the messages provider for messages of providers
   */
  public CommandManager(
      @NonNull String prefix,
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider messagesProvider) {
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
    this.listener = new CommandListener(this, prefix);
    this.globalMiddlewares = new ArrayList<>();
    this.middlewares = new ArrayList<>();
    this.listener.start();
  }

  /**
   * Get a command by one of its aliases.
   *
   * @param name the name or aliases to check if the command has
   * @return a {@link Optional} holding the nullable command. The command that {@link
   *     SystemCommand#hasAlias(String)} matches the parameter name or null if none matches
   */
  @NonNull
  public Optional<SystemCommand> getCommand(@NonNull String name) {
    return this.commands.stream().filter(command -> command.hasAlias(name)).findFirst();
  }

  @Override
  public @NonNull CommandManager register(@NonNull SystemCommand command) {
    this.commands.add(command);
    return this;
  }

  @Override
  public @NonNull List<ReflectSystemCommand> parseCommands(@NonNull Object object) {
    List<ReflectSystemCommand> commands = new ArrayList<>();
    ReflectSystemCommand parent = null;
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
        final ReflectSystemCommand cmd = this.parseCommand(object, method);
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
  public @NonNull ReflectSystemCommand parseCommand(
      @NonNull Object object, @NonNull Method method) {
    Command annotation = method.getAnnotation(Command.class);
    if (annotation == null)
      throw new IllegalArgumentException(
          method + " does not contain the annotation " + Command.class);
    Time time = Time.of(annotation.cooldown());
    return new ReflectSystemCommand(
        this,
        Arrays.asList(annotation.aliases()),
        Option.of(annotation.options()),
        this.getMiddlewares(annotation),
        method,
        object,
        Argument.parseArguments(method),
        new ArrayList<>(),
        !time.isZero() ? new CooldownManager(time) : null);
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
      @NonNull Collection<? extends SystemCommand> commands) {
    return (CommandManager) StarboxCommandManager.super.registerAll(commands);
  }

  @Override
  public @NonNull CommandManager registerAll(@NonNull SystemCommand... commands) {
    return (CommandManager) StarboxCommandManager.super.registerAll(commands);
  }

  @Override
  public void close() {
    commands.clear();
    listener.finish();
  }

  @Override
  public @NonNull StarboxCommandManager<CommandContext, SystemCommand> addGlobalMiddleware(
      @NonNull Middleware<CommandContext>... middlewares) {
    return null;
  }

  @Override
  public @NonNull StarboxCommandManager<CommandContext, SystemCommand> addMiddleware(
      @NonNull Middleware<CommandContext>... middlewares) {
    return null;
  }
}

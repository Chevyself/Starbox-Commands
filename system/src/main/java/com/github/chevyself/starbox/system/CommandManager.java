package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.StarboxCommandManager;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import com.github.chevyself.starbox.system.context.CommandContext;
import com.github.chevyself.starbox.system.middleware.CooldownMiddleware;
import com.github.chevyself.starbox.system.middleware.ResultHandlingMiddleware;
import com.github.chevyself.starbox.time.TimeUtil;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

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
  @NonNull @Getter private final SystemCommandParser parser;
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
    this.parser = new SystemCommandParser(this);
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

  private @NonNull ReflectSystemCommand parseCommand(
      @NonNull Object object, @NonNull Method method, @NonNull Command annotation) {
    Duration duration = TimeUtil.durationOf(annotation.cooldown());
    return new ReflectSystemCommand(
        this,
        Arrays.asList(annotation.aliases()),
        Option.of(annotation.options()),
        this.getMiddlewares(annotation),
        method,
        object,
        Argument.parseArguments(method),
        new ArrayList<>(),
        !duration.isZero() ? new CooldownManager(duration) : null);
  }

  /**
   * Adds the default middlewares.
   *
   * <p>The default middlewares are:
   *
   * <ul>
   *   <li>{@link CooldownMiddleware}
   *   <li>{@link ResultHandlingMiddleware}
   * </ul>
   *
   * @return this same instance
   */
  @NonNull
  public CommandManager addDefaultMiddlewares() {
    this.addGlobalMiddlewares(new CooldownMiddleware(), new ResultHandlingMiddleware());
    return this;
  }

  @NonNull
  private List<Middleware<CommandContext>> getMiddlewares(@NonNull Command command) {
    return StarboxCommandManager.getMiddlewares(
        this.getGlobalMiddlewares(), this.getMiddlewares(), command.include(), command.exclude());
  }

  @Override
  public @NonNull CommandManager registerAllIn(@NonNull String packageName) {
    return (CommandManager) StarboxCommandManager.super.registerAllIn(packageName);
  }

  @Override
  public @NonNull CommandManager parseAndRegister(@NonNull Object object) {
    this.registerAll(this.getParser().parseCommands(object));
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

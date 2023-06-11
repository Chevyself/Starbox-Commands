package com.github.chevyself.starbox;

import com.github.chevyself.starbox.commands.Command;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.adapters.Adapter;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.commands.CommandParserFactory;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.middleware.Middleware;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;

public class CommandManager<C extends StarboxCommandContext<C, T>, T extends Command<C, T>> {

  @NonNull @Getter private final Adapter<C, T> adapter;
  @NonNull @Getter private final CommandParser<C, T> commandParser;
  @NonNull @Getter private final List<T> commands;
  @NonNull @Getter private final ProvidersRegistry<C> providersRegistry;
  @NonNull @Getter private final MessagesProvider<C> messagesProvider;
  @NonNull @Getter private final List<Middleware<C>> globalMiddlewares;
  @NonNull @Getter private final List<Middleware<C>> middlewares;

  private CommandManager(
      @NonNull Adapter<C, T> adapter,
      @NonNull List<T> commands,
      @NonNull CommandParserFactory<C, T> parserFactory,
      @NonNull ProvidersRegistry<C> providersRegistry,
      @NonNull MessagesProvider<C> messagesProvider,
      @NonNull List<Middleware<C>> globalMiddlewares,
      @NonNull List<Middleware<C>> middlewares) {
    this.adapter = adapter;
    this.commandParser = parserFactory.create(this);
    this.commands = commands;
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
    this.globalMiddlewares = globalMiddlewares;
    this.middlewares = middlewares;
  }

  public CommandManager(
      @NonNull Adapter<C, T> adapter,
      @NonNull CommandParserFactory<C, T> parserFactory,
      @NonNull ProvidersRegistry<C> providersRegistry,
      @NonNull MessagesProvider<C> messagesProvider) {
    this(
        adapter,
        new ArrayList<>(),
        parserFactory,
        providersRegistry,
        messagesProvider,
        new ArrayList<>(),
        new ArrayList<>());
  }

  /**
   * Register a new command into the manager. Any command that implements the type T can be
   * registered.
   *
   * @param command the command to be registered
   * @return this same command manager instance to allow chain method calls
   */
  @NonNull
  public CommandManager<C, T> register(@NonNull T command) {
    this.commands.add(command);
    return this;
  }

  /**
   * Register the commands contained inside the class of the provided object. This will execute
   * {@link #parseCommands(Object)} to later {@link #registerAll(Collection)}
   *
   * @param object the object to parse the commands from
   * @return this same command manager instance to allow chain method calls
   */
  @NonNull
  public CommandManager<C, T> parseAndRegister(@NonNull Object object) {
    this.registerAll(this.getCommandParser().parseAllCommandsFrom(object));
    return this;
  }

  /**
   * Register all the objects in an array. This will loop around each object to execute {@link
   * #parseAndRegister(Object)}
   *
   * @param objects the objects to parse and parseAndRegister commands from
   * @return this same command manager instance to allow chain method calls
   */
  @NonNull
  public CommandManager<C, T> parseAndRegisterAll(@NonNull Object... objects) {
    for (Object object : objects) {
      this.parseAndRegister(object);
    }
    return this;
  }

  /**
   * Registers the collection of commands. This will call {@link #register(Command)} on loop
   *
   * @param commands the commands to be registered
   * @return this same command manager instance to allow chain method calls
   */
  @NonNull
  public CommandManager<C, T> registerAll(@NonNull Collection<? extends T> commands) {
    for (T command : commands) {
      this.register(command);
    }
    return this;
  }

  /**
   * Registers the collection of commands. This will call {@link #registerAll(Collection)}
   *
   * @param commands the commands to be registered
   * @return this same command manager instance to allow chain method calls
   * @see #register(Command)
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public CommandManager<C, T> registerAll(@NonNull T... commands) {
    return this.registerAll(Arrays.asList(commands));
  }

  /** Closes the command manager. */
  public void close() {
    this.commands.forEach(this.adapter::onUnregister);
    this.commands.clear();
    this.adapter.close();
  }

  /**
   * Add a global {@link Middleware} to this manager.
   *
   * @param middleware the global middleware to add
   * @return this same instance
   */
  @NonNull
  public CommandManager<C, T> addGlobalMiddleware(@NonNull Middleware<C> middleware) {
    this.globalMiddlewares.add(middleware);
    return this;
  }

  /**
   * Add a {@link Middleware} to this manager.
   *
   * @param middleware the middleware to add
   * @return this same instance
   */
  @NonNull
  public CommandManager<C, T> addMiddleware(@NonNull Middleware<C> middleware) {
    this.middlewares.add(middleware);
    return this;
  }

  /**
   * Registers all the commands in the provided package. This will loop around each class that is
   * annotated with either the command annotation of the module or {@link
   * com.github.chevyself.starbox.annotations.CommandCollection}.
   *
   * <ul>
   *   <li>If the class is annotated with {@link
   *       com.github.chevyself.starbox.annotations.CommandCollection}, then the method {@link
   *       #parseCommands(Object)} will be called to get the commands from the object instance.
   *   <li>If the class is annotated with the command annotation of the module, then a parent
   *       command will be created: if the class contains a method with the annotation {@link
   *       com.github.chevyself.starbox.annotations.ParentOverride} the default parent command logic
   *       will be overridden, this method is treated as any other command method. If there's no
   *       method with such annotation, then a message with the usage of the subcommands will be
   *       sent.
   * </ul>
   *
   * @param packageName the package name to get the commands from
   * @return this same instance
   */
  @NonNull
  public CommandManager<C, T> registerAllIn(@NonNull String packageName) {
    return this.registerAll(this.getCommandParser().parseAllCommandsIn(packageName));
  }

  /**
   * Add many global {@link Middleware} to this manager.
   *
   * @param middlewares the array of global middlewares to add
   * @return this same instance
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public CommandManager<C, T> addGlobalMiddlewares(@NonNull Middleware<C>... middlewares) {
    for (Middleware<C> middleware : middlewares) {
      this.addGlobalMiddleware(middleware);
    }
    return this;
  }

  /**
   * Add many {@link Middleware} to this manager.
   *
   * @param middlewares the array of middlewares to add
   * @return this same instance
   */
  @SuppressWarnings("unchecked")
  @NonNull
  public CommandManager<C, T> addMiddlewares(@NonNull Middleware<C>... middlewares) {
    for (Middleware<C> middleware : middlewares) {
      this.addMiddleware(middleware);
    }
    return this;
  }

  public @NonNull List<Middleware<C>> getMiddlewares(@NonNull com.github.chevyself.starbox.annotations.Command annotation) {
    List<Middleware<C>> list = this.getGlobalMiddlewareAndExclude(annotation);
    list.addAll(this.getIncludeMiddleware(annotation));
    return list;
  }

  private @NonNull List<Middleware<C>> getGlobalMiddlewareAndExclude(
      com.github.chevyself.starbox.annotations.Command annotation) {
    return this.globalMiddlewares.stream()
        .filter(
            middleware -> {
              for (Class<? extends Middleware<?>> clazz : annotation.exclude()) {
                if (clazz.isAssignableFrom(middleware.getClass())) {
                  return false;
                }
              }
              return true;
            })
        .collect(Collectors.toList());
  }

  private @NonNull Collection<? extends Middleware<C>> getIncludeMiddleware(
      com.github.chevyself.starbox.annotations.Command annotation) {
    return middlewares.stream()
        .filter(
            middleware -> {
              for (Class<? extends Middleware<?>> clazz : annotation.include()) {
                if (clazz.isAssignableFrom(middleware.getClass())) {
                  return true;
                }
              }
              return false;
            })
        .collect(Collectors.toList());
  }

  @NonNull
  public Optional<T> getCommand(@NonNull String alias) {
    return this.commands.stream().filter(command -> command.hasAlias(alias)).findFirst();
  }
}

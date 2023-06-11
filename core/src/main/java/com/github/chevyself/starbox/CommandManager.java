package com.github.chevyself.starbox;

import com.github.chevyself.starbox.adapters.Adapter;
import com.github.chevyself.starbox.commands.CommandParserFactory;
import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.context.StarboxCommandContext;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;

public class CommandManager<C extends StarboxCommandContext<C, T>, T extends StarboxCommand<C, T>> {

  @NonNull @Getter private final Adapter<C, T> adapter;
  @NonNull @Getter private final CommandParser<C, T> commandParser;
  @NonNull @Getter private final List<T> commands;
  @NonNull @Getter private final ProvidersRegistry<C> providersRegistry;
  @NonNull @Getter private final MiddlewareRegistry<C> middlewareRegistry;
  @NonNull @Getter private final MessagesProvider<C> messagesProvider;

  private CommandManager(
      @NonNull Adapter<C, T> adapter,
      @NonNull List<T> commands,
      @NonNull CommandParserFactory<C, T> parserFactory,
      @NonNull ProvidersRegistry<C> providersRegistry,
      @NonNull MiddlewareRegistry<C> middlewareRegistry,
      @NonNull MessagesProvider<C> messagesProvider) {
    this.adapter = adapter;
    this.commandParser = parserFactory.create(this);
    this.commands = commands;
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
    this.middlewareRegistry = middlewareRegistry;
  }

  public CommandManager(
      @NonNull Adapter<C, T> adapter,
      @NonNull CommandParserFactory<C, T> parserFactory,
      @NonNull ProvidersRegistry<C> providersRegistry,
      @NonNull MiddlewareRegistry<C> middlewareRegistry,
      @NonNull MessagesProvider<C> messagesProvider) {
    this(
        adapter,
        new ArrayList<>(),
        parserFactory,
        providersRegistry,
        middlewareRegistry,
        messagesProvider);
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
   * Registers the collection of commands. This will call {@link #register(StarboxCommand)} on loop
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
   * @see #register(StarboxCommand)
   */
  @SafeVarargs
  @NonNull
  public final CommandManager<C, T> registerAll(@NonNull T... commands) {
    return this.registerAll(Arrays.asList(commands));
  }

  /** Closes the command manager. */
  public void close() {
    this.commands.forEach(this.adapter::onUnregister);
    this.commands.clear();
    this.adapter.close();
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

  @NonNull
  public Optional<T> getCommand(@NonNull String alias) {
    return this.commands.stream().filter(command -> command.hasAlias(alias)).findFirst();
  }
}

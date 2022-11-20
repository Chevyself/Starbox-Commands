package chevyself.github.commands.jda;

import chevyself.github.commands.Middleware;
import chevyself.github.commands.StarboxCommandManager;
import chevyself.github.commands.annotations.Parent;
import chevyself.github.commands.arguments.Argument;
import chevyself.github.commands.flags.Option;
import chevyself.github.commands.jda.annotations.Command;
import chevyself.github.commands.jda.annotations.Entry;
import chevyself.github.commands.jda.context.CommandContext;
import chevyself.github.commands.jda.context.GenericCommandContext;
import chevyself.github.commands.jda.cooldown.CooldownManager;
import chevyself.github.commands.jda.listener.CommandListener;
import chevyself.github.commands.jda.messages.JdaMessagesProvider;
import chevyself.github.commands.jda.messages.MessagesProvider;
import chevyself.github.commands.jda.middleware.PermissionMiddleware;
import chevyself.github.commands.jda.providers.registry.JdaProvidersRegistry;
import chevyself.github.commands.jda.result.Result;
import chevyself.github.commands.providers.registry.ProvidersRegistry;
import chevyself.github.commands.providers.type.StarboxContextualProvider;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * The command that are registered inside this manager makes them work in the {@link #listener} this
 * means that the {@link CommandListener} will execute the command when the bot receives a message
 * {@link CommandListener#onMessageReceived(MessageReceivedEvent)}
 *
 * <p>The easiest way to create commands is using reflection with the method {@link
 * #parseCommands(Object)} those parsed commands can be later registered in this instance using
 * {@link #registerAll(Collection)}.
 *
 * <p>To create a {@link CommandManager} instance you simply need a {@link ProvidersRegistry} you
 * can use {@link JdaProvidersRegistry} which includes some providers that are intended for JDA use
 * you can even extend it to add more in the constructor or use {@link
 * ProvidersRegistry#addProvider(StarboxContextualProvider)}, you also need a {@link
 * MessagesProvider} which is used to display error commands the default implementation is {@link
 * JdaMessagesProvider}, to check the permissions of the {@link net.dv8tion.jda.api.entities.User}
 * that execute the command you can use the {@link PermissionMiddleware} which only checks for
 * {@link net.dv8tion.jda.api.Permission}. The instance of {@link JDA} is required to
 * #parseAndRegister. The {@link CommandListener}, the {@link ListenerOptions} changes some logic
 * inside {@link CommandListener} and finally the prefix is the {@link String} that must contain the
 * message at the start
 *
 * <pre>{@code
 * JDA jda = ...
 * MessagesProvider messagesProvider = ...
 * new CommandManager(new JdaProvidersRegistry(messagesProvider), messagesProvider, () -&gt; messagesProvider, jda, new ListenerOptions(), "-");
 * }</pre>
 */
public class CommandManager implements StarboxCommandManager<CommandContext, JdaCommand> {

  @NonNull @Getter private final List<JdaCommand> commands = new ArrayList<>();
  @NonNull @Getter private final Map<Long, List<JdaCommand>> guildCommands = new HashMap<>();
  @NonNull @Getter private final JDA jda;
  @NonNull @Getter private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter private final List<Middleware<CommandContext>> globalMiddlewares;
  @NonNull @Getter private final List<Middleware<CommandContext>> middlewares;
  @NonNull @Getter private final CommandListener listener;
  @NonNull @Getter private final ListenerOptions listenerOptions;

  /**
   * Create an instance.
   *
   * @param providersRegistry the providers' registry to provide the array of {@link Object} to
   *     invoke {@link AnnotatedCommand} using reflection or to be used in {@link
   *     GenericCommandContext}
   * @param messagesProvider the messages provider for important messages
   * @param jda the instance to parseAndRegister the {@link #listener} on
   * @param listenerOptions to change some login in the {@link #listener}
   */
  public CommandManager(
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider messagesProvider,
      @NonNull JDA jda,
      @NonNull ListenerOptions listenerOptions) {
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
    this.jda = jda;
    this.globalMiddlewares = new ArrayList<>();
    this.middlewares = new ArrayList<>();
    this.listenerOptions = listenerOptions;
    this.listener = new CommandListener(this, listenerOptions, messagesProvider);
    jda.addEventListener(this.listener);
  }

  /**
   * Get the command instance that matches the name and guild. This will loop through all the {@link
   * #guildCommands} and {@link #commands} until one is {@link JdaCommand#hasAlias(String)} = true
   *
   * @param guild the guild to find the command
   * @param name the name to match the command
   * @return the instance of the command if found else null
   */
  public JdaCommand getCommand(Guild guild, @NonNull String name) {
    return this.getCommand(guild == null ? 0 : guild.getIdLong(), name);
  }

  /**
   * Get the command instance that matches the name. This will loop through all the {@link
   * #commands} until one is {@link JdaCommand#hasAlias(String)} = true.
   *
   * @param name the name to match the command
   * @return the instance of the command if found else null
   */
  public JdaCommand getCommand(@NonNull String name) {
    return this.getCommand(0L, name);
  }

  /**
   * Get the command instance that matches the name and guild. This will loop through all the {@link
   * #guildCommands} and {@link #commands} until one is {@link JdaCommand#hasAlias(String)} = true
   *
   * @param guild the id of the guild to find the command
   * @param name the name to match the command
   * @return the instance of the command if found else null
   */
  public JdaCommand getCommand(long guild, @NonNull String name) {
    if (guild > 0) {
      List<JdaCommand> commands = this.guildCommands.get(guild);
      if (commands != null) {
        for (JdaCommand command : commands) {
          if (command.hasAlias(name)) {
            return command;
          }
        }
      }
    }
    for (JdaCommand command : this.commands) {
      if (command.hasAlias(name)) {
        return command;
      }
    }
    return null;
  }

  @Override
  public @NonNull CommandManager register(@NonNull JdaCommand command) {
    this.commands.add(command);
    this.jda.upsertCommand(command.getCommandData());
    return this;
  }

  /**
   * Register a command inside this manager that will only run in an assigned guild.
   *
   * @param guild the guild that will be allowed to use the command
   * @param command the command to register
   * @return this same instance
   */
  public @NonNull CommandManager register(@NonNull Guild guild, @NonNull JdaCommand command) {
    long id = guild.getIdLong();
    this.getCommands(guild).add(command);
    guild.upsertCommand(command.getCommandData()).queue();
    return this;
  }

  @NonNull
  private List<JdaCommand> getCommands(@NonNull Guild guild) {
    return this.guildCommands.computeIfAbsent(guild.getIdLong(), id -> new ArrayList<>());
  }

  @Override
  public @NonNull List<AnnotatedCommand> parseCommands(@NonNull Object object) {
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
    Command annotation = method.getAnnotation(Command.class);
    return new AnnotatedCommand(
        this,
        annotation.description(),
        this.getMap(annotation),
        Option.of(annotation.options()),
        this.getMiddlewares(annotation),
        CooldownManager.of(annotation).orElse(null),
        Arrays.asList(annotation.aliases()),
        method,
        object,
        Argument.parseArguments(method));
  }

  @NonNull
  private Map<String, String> getMap(@NonNull Command annotation) {
    Map<String, String> map = new HashMap<>();
    for (Entry entry : annotation.map()) {
      map.put(entry.key(), entry.value());
    }
    return map;
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
  public @NonNull CommandManager registerAll(@NonNull Collection<? extends JdaCommand> commands) {
    return (CommandManager) StarboxCommandManager.super.registerAll(commands);
  }

  @Override
  public @NonNull CommandManager registerAll(@NonNull JdaCommand... commands) {
    return (CommandManager) StarboxCommandManager.super.registerAll(commands);
  }

  /**
   * Parse and register and commands that will only run in an assigned guild.
   *
   * @param guild the guild that will be allowed to use the commands
   * @param object the object to parse the commands
   * @return this same instance
   */
  public @NonNull CommandManager parseAndRegister(@NonNull Guild guild, @NonNull Object object) {
    return this.registerAll(guild, this.parseCommands(object));
  }

  /**
   * Parse and register and commands that will only run in an assigned guild.
   *
   * @param guild the guild that will be allowed to use the commands
   * @param objects the objects to parse the commands
   * @return this same instanceK
   */
  public @NonNull CommandManager parseAndRegisterAll(
      @NonNull Guild guild, @NonNull Object... objects) {
    return this.registerAll(guild, this.parseCommands(objects));
  }

  /**
   * Register a collection of commands inside this manager that will only run in an assigned guild.
   *
   * @param guild the guild that will be allowed to use the commands
   * @param commands the collection to register
   * @return this same instance
   */
  public @NonNull CommandManager registerAll(
      @NonNull Guild guild, @NonNull Collection<? extends JdaCommand> commands) {
    for (JdaCommand command : commands) {
      this.register(guild, command);
    }
    return this;
  }

  /**
   * Register a collection of commands inside this manager that will only run in an assigned guild.
   *
   * @param guild the guild that will be allowed to use the commands
   * @param commands the collection to register
   * @return this same instance
   */
  public @NonNull CommandManager registerAll(
      @NonNull Guild guild, @NonNull JdaCommand... commands) {
    return this.registerAll(guild, Arrays.asList(commands));
  }

  @Override
  public void close() {
    this.commands.clear();
    jda.removeEventListener(listener);
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

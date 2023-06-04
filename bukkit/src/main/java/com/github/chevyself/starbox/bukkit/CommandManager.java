package com.github.chevyself.starbox.bukkit;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.StarboxCommandManager;
import com.github.chevyself.starbox.bukkit.annotations.Command;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.messages.BukkitMessagesProvider;
import com.github.chevyself.starbox.bukkit.messages.MessagesProvider;
import com.github.chevyself.starbox.bukkit.middleware.CooldownMiddleware;
import com.github.chevyself.starbox.bukkit.middleware.PermissionMiddleware;
import com.github.chevyself.starbox.bukkit.middleware.ResultHandlingMiddleware;
import com.github.chevyself.starbox.bukkit.providers.registry.BukkitProvidersRegistry;
import com.github.chevyself.starbox.bukkit.topic.PluginHelpTopic;
import com.github.chevyself.starbox.bukkit.topic.StarboxCommandHelpTopicFactory;
import com.github.chevyself.starbox.bukkit.utils.BukkitUtils;
import com.github.chevyself.starbox.exceptions.CommandRegistrationException;
import com.github.chevyself.starbox.providers.registry.ProvidersRegistry;
import com.github.chevyself.starbox.providers.type.StarboxContextualProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
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
 * related to the commands, a {@link ProvidersRegistry} you can use {@link BukkitProvidersRegistry}
 * which includes some providers that are intended for Bukkit use you can even extend it to add more
 * in the constructor or use {@link ProvidersRegistry#addProvider(StarboxContextualProvider)}, you
 * also need a {@link MessagesProvider} which is mostly used to display error commands or create the
 * {@link org.bukkit.help.HelpTopic} for the commands registered in this manager to be added inside
 * the built-in bukkit command "/help" the default implementation is {@link BukkitMessagesProvider}:
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
   * reflection through the method {@link BukkitUtils#getCommandMap()}
   */
  @NonNull private static final CommandMap commandMap;

  static {
    try {
      commandMap = BukkitUtils.getCommandMap();
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new CommandRegistrationException("CommandMap could not be accessed");
    }
  }

  @NonNull @Getter private final List<StarboxBukkitCommand> commands = new ArrayList<>();
  @NonNull @Getter private final Plugin plugin;
  @NonNull @Getter private final ProvidersRegistry<CommandContext> providersRegistry;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter private final List<Middleware<CommandContext>> globalMiddlewares;
  @NonNull @Getter private final List<Middleware<CommandContext>> middlewares;
  @NonNull @Getter private final BukkitCommandParser parser;

  /**
   * Create an instance.
   *
   * @param plugin the plugin that is related to the commands and other Bukkit actions such as
   *     creating tasks with the {@link org.bukkit.scheduler.BukkitScheduler}
   * @param providersRegistry the providers' registry to provide the array of {@link Object} to
   *     invoke {@link AnnotatedCommand} using reflection or to be used in {@link CommandContext}
   * @param messagesProvider the messages' provider for important messages and {@link
   *     org.bukkit.help.HelpTopic} of commands and the "plugin"
   */
  public CommandManager(
      @NonNull Plugin plugin,
      @NonNull ProvidersRegistry<CommandContext> providersRegistry,
      @NonNull MessagesProvider messagesProvider) {
    this.plugin = plugin;
    this.providersRegistry = providersRegistry;
    this.messagesProvider = messagesProvider;
    this.globalMiddlewares = new ArrayList<>();
    this.middlewares = new ArrayList<>();
    this.parser = new BukkitCommandParser(this);
  }

  /**
   * Registers {@link StarboxCommandHelpTopicFactory} into the factory.
   *
   * @return this same instance
   */
  @NonNull
  public CommandManager registerHelpFactory() {
    CommandManager.helpMap.registerHelpTopicFactory(
        StarboxBukkitCommand.class, new StarboxCommandHelpTopicFactory(this.messagesProvider));
    return this;
  }

  /**
   * Registers {@link #plugin} inside the {@link HelpMap}. You can learn more about this in {@link
   * PluginHelpTopic} but basically this will make possible to do: "/help [plugin-name]"
   *
   * @return this same instance
   */
  @NonNull
  public CommandManager registerPlugin() {
    CommandManager.helpMap.addTopic(new PluginHelpTopic(this.plugin, this, this.messagesProvider));
    return this;
  }

  @NonNull
  @Override
  public CommandManager register(@NonNull StarboxBukkitCommand command) {
    CommandManager.commandMap.register(this.plugin.getName(), command);
    this.commands.add(command);
    return this;
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
      @NonNull Collection<? extends StarboxBukkitCommand> commands) {
    return (CommandManager) StarboxCommandManager.super.registerAll(commands);
  }

  @Override
  public @NonNull CommandManager registerAll(@NonNull StarboxBukkitCommand... commands) {
    return (CommandManager) StarboxCommandManager.super.registerAll(commands);
  }

  @Override
  public @NonNull CommandManager registerAllIn(@NonNull String packageName) {
    return (CommandManager) StarboxCommandManager.super.registerAllIn(packageName);
  }

  @Override
  public void close() {
    this.commands.forEach(command -> command.unregister(CommandManager.commandMap));
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

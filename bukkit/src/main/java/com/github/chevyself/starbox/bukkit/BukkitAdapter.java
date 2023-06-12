package com.github.chevyself.starbox.bukkit;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.CommandManagerBuilder;
import com.github.chevyself.starbox.adapters.Adapter;
import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.messages.BukkitMessagesProvider;
import com.github.chevyself.starbox.bukkit.messages.GenericBukkitMessagesProvider;
import com.github.chevyself.starbox.bukkit.middleware.BukkitResultHandlingMiddleware;
import com.github.chevyself.starbox.bukkit.middleware.PermissionMiddleware;
import com.github.chevyself.starbox.bukkit.providers.BukkitCommandContextProvider;
import com.github.chevyself.starbox.bukkit.providers.CommandSenderArgumentProvider;
import com.github.chevyself.starbox.bukkit.providers.MaterialProvider;
import com.github.chevyself.starbox.bukkit.providers.OfflinePlayerProvider;
import com.github.chevyself.starbox.bukkit.providers.PlayerProvider;
import com.github.chevyself.starbox.bukkit.topic.PluginHelpTopic;
import com.github.chevyself.starbox.bukkit.topic.StarboxCommandHelpTopicFactory;
import com.github.chevyself.starbox.bukkit.utils.BukkitUtils;
import com.github.chevyself.starbox.exceptions.CommandRegistrationException;
import com.github.chevyself.starbox.messages.MessagesProvider;
import com.github.chevyself.starbox.parsers.CommandMetadataParser;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.parsers.EmptyCommandMetadataParser;
import com.github.chevyself.starbox.registry.MiddlewareRegistry;
import com.github.chevyself.starbox.registry.ProvidersRegistry;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.help.HelpMap;
import org.bukkit.plugin.Plugin;

public class BukkitAdapter implements Adapter<CommandContext, BukkitCommand> {

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

  @NonNull @Getter private final Plugin plugin;
  private final boolean registerPluginHelp;

  public BukkitAdapter(@NonNull Plugin plugin, boolean registerPluginHelp) {
    this.plugin = plugin;
    this.registerPluginHelp = registerPluginHelp;
  }

  @Override
  public void onRegister(@NonNull BukkitCommand command) {
    BukkitAdapter.commandMap.register(this.plugin.getName(), command.getExecutor());
  }

  @Override
  public void onUnregister(@NonNull BukkitCommand command) {}

  @Override
  public void close() {}

  @Override
  public void registerDefaultProviders(
      @NonNull CommandManagerBuilder<CommandContext, BukkitCommand> builder,
      @NonNull ProvidersRegistry<CommandContext> registry) {
    MessagesProvider<CommandContext> provider = builder.getMessagesProvider();
    if (provider instanceof BukkitMessagesProvider) {
      BukkitMessagesProvider bukkit = (BukkitMessagesProvider) provider;
      registry
          .addProvider(new MaterialProvider(bukkit))
          .addProvider(new OfflinePlayerProvider())
          .addProvider(new PlayerProvider(bukkit));
    } else {
      this.plugin
          .getLogger()
          .severe(
              "Failed to register some providers, as the MessagesProvider is not a BukkitMessagesProvider");
    }
    registry
        .addProvider(new BukkitCommandContextProvider())
        .addProvider(new CommandSenderArgumentProvider());
  }

  @Override
  public void registerDefaultMiddlewares(
      @NonNull CommandManagerBuilder<CommandContext, BukkitCommand> builder,
      @NonNull MiddlewareRegistry<CommandContext> middlewares) {
    MessagesProvider<CommandContext> provider = builder.getMessagesProvider();
    if (provider instanceof BukkitMessagesProvider) {
      BukkitMessagesProvider bukkit = (BukkitMessagesProvider) provider;
      middlewares.addGlobalMiddleware(new PermissionMiddleware(bukkit));
    } else {
      this.plugin
          .getLogger()
          .severe(
              "Failed to register some middlewares, as the MessagesProvider is not a BukkitMessagesProvider");
    }
    middlewares.addGlobalMiddleware(new BukkitResultHandlingMiddleware());
  }

  @Override
  public void onBuilt(@NonNull CommandManager<CommandContext, BukkitCommand> built) {
    MessagesProvider<CommandContext> provider = built.getMessagesProvider();
    if (registerPluginHelp) {
      if (provider instanceof BukkitMessagesProvider) {
        this.registerPlugin(built, (BukkitMessagesProvider) provider);
        this.registerHelpFactory((BukkitMessagesProvider) provider);
      } else {
        this.plugin
            .getLogger()
            .severe(
                "Failed to register the plugin help map, as the MessagesProvider is not a BukkitMessagesProvider");
      }
    }
  }

  /**
   * Registers {@link #plugin} inside the {@link HelpMap}. You can learn more about this in {@link
   * PluginHelpTopic} but basically this will make possible to do: "/help [plugin-name]"
   *
   * @return this same instance
   */
  private void registerPlugin(
      @NonNull CommandManager<CommandContext, BukkitCommand> commandManager,
      @NonNull BukkitMessagesProvider messagesProvider) {
    BukkitAdapter.helpMap.addTopic(
        new PluginHelpTopic(this.plugin, commandManager, messagesProvider));
  }

  private void registerHelpFactory(@NonNull BukkitMessagesProvider messagesProvider) {
    BukkitAdapter.helpMap.registerHelpTopicFactory(
        BukkitCommand.class, new StarboxCommandHelpTopicFactory(messagesProvider));
  }

  @Override
  public @NonNull CommandParser<CommandContext, BukkitCommand> createParser(
      @NonNull CommandManager<CommandContext, BukkitCommand> commandManager) {
    return new BukkitCommandParser(this, commandManager);
  }

  @Override
  public @NonNull CommandMetadataParser getDefaultCommandMetadataParser() {
    return new EmptyCommandMetadataParser();
  }

  @Override
  public @NonNull MessagesProvider<CommandContext> getDefaultMessaesProvider() {
    return new GenericBukkitMessagesProvider();
  }
}

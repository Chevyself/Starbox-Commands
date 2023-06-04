package com.github.chevyself.starbox.bungee;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.StarboxCommand;
import com.github.chevyself.starbox.annotations.Parent;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.result.BungeeResult;
import com.github.chevyself.starbox.flags.CommandLineParser;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.util.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

/**
 * This is the direct implementation of {@link StarboxCommand} for the "Bungee" module extending
 * this class allows to parseAndRegister commands in the {@link CommandManager} using {@link
 * CommandManager#register(BungeeCommand)} the creation of a reflection command using {@link
 * CommandManager#parseCommands(Object)} returns a {@link AnnotatedCommand}
 *
 * <p>To parse {@link AnnotatedCommand} is required to use the annotation {@link
 * com.github.chevyself.starbox.bungee.annotations.Command} if you would like to create an extension
 * the method to override is {@link #execute(CommandContext)} but also you can override {@link
 * #onTabComplete(CommandSender, String[])} at the moment the only tab complete will only return the
 * names of the children commands {@link #getChildren()} {@link #getChildrenNames()}
 *
 * <p>This also allows to use the command asynchronously check {@link #BungeeCommand(String, List,
 * CommandManager, List, List, boolean, CooldownManager)} or {@link #BungeeCommand(String, String,
 * List, CommandManager, List, List, boolean, CooldownManager, String...)}
 */
public abstract class BungeeCommand extends Command
    implements StarboxCommand<CommandContext, BungeeCommand>, TabExecutor {

  @NonNull @Getter protected final CommandManager manager;
  @NonNull @Getter protected final List<Option> options;
  @NonNull @Getter protected final List<Middleware<CommandContext>> middlewares;
  protected final boolean async;
  @NonNull @Getter private final List<BungeeCommand> children;
  private final CooldownManager cooldown;

  /**
   * Create the command.
   *
   * @param name the name of the command
   * @param children the list of children commands which can be used with this parent prefix. Learn
   *     more in {@link Parent}
   * @param manager where the command will be registered used to get the {@link
   *     CommandManager#getMessagesProvider()} and {@link CommandManager#getProvidersRegistry()}
   * @param options the flags that apply in this command
   * @param middlewares the middlewares to run before and after this command is executed
   * @param async Whether the command should {{@link #execute(CommandContext)}} async. To know more
   *     about asynchronization check <a
   *     href="https://bukkit.fandom.com/wiki/Scheduler_Programming">Bukkit wiki</a>
   * @param cooldown the manager that handles the cooldown in this command
   */
  public BungeeCommand(
      String name,
      @NonNull List<BungeeCommand> children,
      @NonNull CommandManager manager,
      @NonNull List<Option> options,
      @NonNull List<Middleware<CommandContext>> middlewares,
      boolean async,
      CooldownManager cooldown) {
    super(name);
    this.children = children;
    this.manager = manager;
    this.options = options;
    this.middlewares = middlewares;
    this.async = async;
    this.cooldown = cooldown;
  }

  /**
   * Create the command.
   *
   * @param name the name of the command
   * @param permission the permission that the sender requires to execute the command {@link
   *     CommandSender#hasPermission(String)}
   * @param children the list of children commands which can be used with this parent prefix. Learn
   *     more in {@link Parent}
   * @param manager where the command will be registered used to get the {@link
   *     CommandManager#getMessagesProvider()} and {@link CommandManager#getProvidersRegistry()}
   * @param options the flags that apply in this command
   * @param middlewares the middlewares to run before and after this command is executed
   * @param async Whether the command should {{@link #execute(CommandContext)}} async. To know more
   *     about asynchronization check <a
   *     href="https://bukkit.fandom.com/wiki/Scheduler_Programming">Bukkit wiki</a>
   * @param cooldown the manager that handles the cooldown in this command
   * @param aliases the aliases which also allow to execute the command
   */
  public BungeeCommand(
      String name,
      String permission,
      @NonNull List<BungeeCommand> children,
      @NonNull CommandManager manager,
      @NonNull List<Option> options,
      @NonNull List<Middleware<CommandContext>> middlewares,
      boolean async,
      CooldownManager cooldown,
      String... aliases) {
    super(name, permission, aliases);
    this.children = children;
    this.manager = manager;
    this.options = options;
    this.middlewares = middlewares;
    this.async = async;
    this.cooldown = cooldown;
  }

  /**
   * Checks if the command should be running async if so it will create the task with {@link
   * net.md_5.bungee.api.scheduler.TaskScheduler#runAsync(Plugin, Runnable)} these uses the {@link
   * CommandManager} plugin
   *
   * @param sender the executor of the command
   * @param parser the arguments used in the command execution
   */
  public void runCheckSync(@NonNull CommandSender sender, @NonNull CommandLineParser parser) {
    if (this.async) {
      ProxyServer.getInstance()
          .getScheduler()
          .runAsync(this.manager.getPlugin(), () -> this.run(sender, parser));
    } else {
      this.run(sender, parser);
    }
  }

  /**
   * This method does the command execution after {@link #runCheckSync(CommandSender,
   * CommandLineParser)} finishes checking whether to run async or not.
   *
   * <p>This calls {@link #execute(CommandContext)} and the {@link BungeeResult} will be sent to the
   * {@link CommandSender} if it is not null with {@link CommandSender#sendMessage(BaseComponent)}
   * and {@link BungeeResult} components
   *
   * @param sender the executor of the command
   * @param parser the arguments used in the command execution
   */
  public void run(@NonNull CommandSender sender, @NonNull CommandLineParser parser) {
    CommandContext context =
        new CommandContext(
            parser,
            this,
            sender,
            this.manager.getProvidersRegistry(),
            this.manager.getMessagesProvider());
    BungeeResult result =
        this.getMiddlewares().stream()
            .map(middleware -> middleware.next(context))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .map(
                starboxResult -> {
                  // Here maybe thrown an error because the wrong result was provided
                  return starboxResult instanceof BungeeResult
                      ? (BungeeResult) starboxResult
                      : null;
                })
            .orElseGet(() -> this.execute(context));
    this.getMiddlewares().forEach(middleware -> middleware.next(context, result));
  }

  /**
   * Get the name of the children that have been added inside the command {@link #getChildren()}.
   *
   * @return the list with the names of the children commands
   */
  @NonNull
  public List<String> getChildrenNames() {
    List<String> names = new ArrayList<>();
    for (BungeeCommand child : this.getChildren()) {
      names.add(child.getName());
    }
    return names;
  }

  @Override
  public void execute(CommandSender sender, String[] strings) {
    this.execute(sender, CommandLineParser.parse(this.options, strings));
  }

  private void execute(@NonNull CommandSender sender, @NonNull CommandLineParser parser) {
    List<String> arguments = parser.getArguments();
    if (arguments.size() >= 1) {
      Optional<BungeeCommand> optionalCommand = this.getChildren(arguments.get(0));
      if (optionalCommand.isPresent()) {
        BungeeCommand command = optionalCommand.get();
        command.execute(sender, parser.copyFrom(1, command.getOptions()));
        return;
      }
    }
    this.runCheckSync(sender, parser);
  }

  @Override
  public abstract BungeeResult execute(@NonNull CommandContext context);

  @Override
  public boolean hasAlias(@NonNull String alias) {
    if (this.getName().equalsIgnoreCase(alias)) {
      return true;
    }
    for (String name : this.getAliases()) {
      if (name.equalsIgnoreCase(alias)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public @NonNull Optional<CooldownManager> getCooldownManager() {
    return Optional.ofNullable(cooldown);
  }

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] strings) {
    if (this.getPermission() != null && !sender.hasPermission(this.getPermission())) {
      return new ArrayList<>();
    }
    if (strings.length == 1) {
      return Strings.copyPartials(strings[strings.length - 1], this.getChildrenNames());
    } else if (strings.length >= 2) {
      return this.getChildren(strings[0])
          .map(
              command ->
                  command.onTabComplete(sender, Arrays.copyOfRange(strings, 1, strings.length)))
          .orElseGet(ArrayList::new);
    }
    return new ArrayList<>();
  }
}

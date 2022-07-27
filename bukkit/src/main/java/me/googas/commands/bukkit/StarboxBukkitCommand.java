package me.googas.commands.bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.Middleware;
import me.googas.commands.StarboxCommand;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.result.BukkitResult;
import me.googas.commands.flags.FlagArgument;
import me.googas.commands.flags.Option;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;

/**
 * This is the direct implementation of {@link StarboxCommand} for the "Bukkit" module extending
 * this class allows to parseAndRegister commands in the {@link CommandManager} using {@link
 * CommandManager#register(StarboxBukkitCommand)} the creation of a reflection command using {@link
 * CommandManager#parseCommands(Object)} returns a {@link AnnotatedCommand}
 *
 * <p>To parse {@link AnnotatedCommand} is required to use the annotation {@link
 * me.googas.commands.bukkit.annotations.Command} if you would like to create an extension the
 * method to override is {@link #execute(CommandContext)} but also you can override {@link
 * #tabComplete(CommandSender, String, String[])} at the moment the only tab complete will only
 * return the names of the children commands {@link #getChildren()}
 *
 * <p>This also allows to use the command asynchronously check {@link
 * #StarboxBukkitCommand(CommandManager, String, List, String, String, List, List, boolean,
 * CooldownManager)} or {@link #StarboxBukkitCommand(CommandManager, String, List, List, boolean,
 * CooldownManager)}
 */
public abstract class StarboxBukkitCommand extends Command
    implements StarboxCommand<CommandContext, StarboxBukkitCommand> {

  @NonNull @Getter protected final CommandManager manager;
  @NonNull @Getter protected final List<Option> options;
  @NonNull @Getter protected final List<Middleware<CommandContext>> middlewares;
  protected final boolean async;
  private final CooldownManager cooldown;

  /**
   * Create the command.
   *
   * @param manager where the command will be registered used to get the {@link
   *     CommandManager#getMessagesProvider()} and {@link CommandManager#getProvidersRegistry()}
   * @param name the name of the command
   * @param options the flags that apply in this command
   * @param middlewares the middlewares to run before and after this command is executed
   * @param async Whether the command should {{@link #execute(CommandContext)}} async. To know more
   *     about asynchronization check <a
   *     href="https://bukkit.fandom.com/wiki/Scheduler_Programming">Bukkit wiki</a>
   * @param cooldown the manager that handles the cooldown in this command
   */
  public StarboxBukkitCommand(
      @NonNull CommandManager manager,
      @NonNull String name,
      @NonNull List<Option> options,
      @NonNull List<Middleware<CommandContext>> middlewares,
      boolean async,
      CooldownManager cooldown) {
    super(name);
    this.middlewares = middlewares;
    this.async = async;
    this.manager = manager;
    this.options = options;
    this.cooldown = cooldown;
  }

  /**
   * Create the command.
   *
   * @param manager where the command will be registered used to get the {@link
   *     CommandManager#getMessagesProvider()} and {@link CommandManager#getProvidersRegistry()}
   * @param name the name of the command
   * @param aliases the aliases which also allow to execute the command
   * @param description a simple description of the command
   * @param usageMessage a message describing how the message should execute. You can learn more
   *     about usage messages in {@link me.googas.commands.arguments.Argument}
   * @param options the flags that apply in this command
   * @param middlewares the middlewares to run before and after this command is executed
   * @param async Whether the command should {{@link #execute(CommandContext)}} async. To know more
   *     about asynchronization check <a
   *     href="https://bukkit.fandom.com/wiki/Scheduler_Programming">Bukkit wiki</a>
   * @param cooldown the manager that handles the cooldown in this command
   */
  public StarboxBukkitCommand(
      @NonNull CommandManager manager,
      @NonNull String name,
      @NonNull List<String> aliases,
      @NonNull String description,
      @NonNull String usageMessage,
      @NonNull List<Option> options,
      @NonNull List<Middleware<CommandContext>> middlewares,
      boolean async,
      CooldownManager cooldown) {
    super(name, description, usageMessage, aliases);
    this.middlewares = middlewares;
    this.async = async;
    this.manager = manager;
    this.options = options;
    this.cooldown = cooldown;
  }

  /**
   * This method does the command execution after {@link #runCheckSync(CommandSender, String[])}
   * finishes checking whether to run async or not.
   *
   * <p>This calls {@link #execute(CommandContext)} and the {@link BukkitResult} will be sent to the
   * {@link CommandSender} if it is not null with {@link CommandSender#sendMessage(String)} and
   * {@link BukkitResult} components
   *
   * @param sender the executor of the command
   * @param args the arguments used in the command execution
   */
  public void run(@NonNull CommandSender sender, @NonNull String[] args) {
    FlagArgument.Parser parse = FlagArgument.parse(this.getOptions(), args);
    CommandContext context =
        new CommandContext(
            this,
            sender,
            parse.getArgumentsString(),
            parse.getArgumentsArray(),
            this.manager.getProvidersRegistry(),
            this.manager.getMessagesProvider(),
            parse.getFlags());
    BukkitResult result =
        this.getMiddlewares().stream()
            .map(middleware -> middleware.next(context))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .map(
                starboxResult -> {
                  // Here maybe thrown an error because the wrong result was provided
                  return starboxResult instanceof BukkitResult
                      ? (BukkitResult) starboxResult
                      : null;
                })
            .orElseGet(() -> this.execute(context));
    this.getMiddlewares().forEach(middleware -> middleware.next(context, result));
  }

  /**
   * Checks if the command should be running async if so it will create the task with {@link
   * org.bukkit.scheduler.BukkitScheduler#runTaskAsynchronously(Plugin, Runnable)}. This uses the
   * {@link CommandManager} plugin
   *
   * @param sender the executor of the command
   * @param args the arguments used in the command execution
   */
  public void runCheckSync(@NonNull CommandSender sender, @NonNull String[] args) {
    if (this.async) {
      Bukkit.getScheduler()
          .runTaskAsynchronously(this.manager.getPlugin(), () -> this.run(sender, args));
    } else {
      this.run(sender, args);
    }
  }

  /**
   * Get the name of the children that have been added inside the command {@link #getChildren()}.
   *
   * @return the list with the names of the children commands
   */
  @NonNull
  public List<String> getChildrenNames() {
    List<String> names = new ArrayList<>();
    for (StarboxBukkitCommand child : this.getChildren()) {
      names.add(child.getName());
    }
    return names;
  }

  @Override
  public boolean execute(
      @NonNull CommandSender sender, @NonNull String alias, String @NonNull [] strings) {
    if (strings.length >= 1) {
      Optional<StarboxBukkitCommand> command = this.getChildren(strings[0]);
      if (command.isPresent()) {
        return command.get().execute(sender, alias, Arrays.copyOfRange(strings, 1, strings.length));
      }
    }
    this.runCheckSync(sender, strings);
    return true;
  }

  @Override
  public @NonNull List<String> tabComplete(
      @NonNull CommandSender sender, @NonNull String alias, String @NonNull [] strings)
      throws IllegalArgumentException {
    if (this.getPermission() != null && !sender.hasPermission(this.getPermission())) {
      return new ArrayList<>();
    }
    if (strings.length == 1) {
      return StringUtil.copyPartialMatches(
          strings[strings.length - 1], this.getChildrenNames(), new ArrayList<>());
    } else if (strings.length >= 2) {
      final Optional<StarboxBukkitCommand> optionalCommand = this.getChildren(strings[0]);
      return optionalCommand
          .map(
              starboxBukkitCommand ->
                  starboxBukkitCommand.tabComplete(
                      sender, alias, Arrays.copyOfRange(strings, 1, strings.length)))
          .orElseGet(ArrayList::new);
    }
    return new ArrayList<>();
  }

  @Override
  public abstract BukkitResult execute(@NonNull CommandContext context);

  @Override
  public @NonNull Optional<CooldownManager> getCooldownManager() {
    return Optional.ofNullable(cooldown);
  }
}

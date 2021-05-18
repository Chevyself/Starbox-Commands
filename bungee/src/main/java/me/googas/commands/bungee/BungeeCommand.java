package me.googas.commands.bungee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.EasyCommand;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.result.Result;
import me.googas.utility.Strings;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

/**
 * This is the direct implementation of {@link EasyCommand} for the "Bungee" module extending this
 * class allows to parseAndRegister commands in the {@link CommandManager} using {@link
 * CommandManager#register(BungeeCommand)} the creation of a reflection command using {@link
 * CommandManager#parseCommands(Object)} returns a {@link AnnotatedCommand}
 *
 * <p>To parse {@link AnnotatedCommand} is required to use the annotation {@link
 * me.googas.commands.bungee.annotations.Command} if you would like to create an extension the
 * method to override is {@link #execute(CommandContext)} but also you can override {@link
 * #onTabComplete(CommandSender, String[])} at the moment the only tab complete will only return the
 * names of the children commands {@link #getChildren()} {@link #getChildrenNames()}
 *
 * <p>This also allows to use the command asynchronously check {@link #BungeeCommand(String, List,
 * CommandManager, boolean)} or {@link #BungeeCommand(String, String, List, CommandManager, boolean,
 * String...)}
 */
public abstract class BungeeCommand extends Command
    implements EasyCommand<CommandContext, BungeeCommand>, TabExecutor {

  @NonNull @Getter protected final CommandManager manager;
  @NonNull @Getter private final List<BungeeCommand> children;
  protected boolean async;

  /**
   * Create the command
   *
   * @param name the name of the command
   * @param children the list of children commands which can be used with this parent prefix. Learn
   *     more in {@link me.googas.commands.annotations.Parent}
   * @param manager where the command will be registered used to get the {@link
   *     CommandManager#getMessagesProvider()} and {@link CommandManager#getProvidersRegistry()}
   * @param async Whether the command should {{@link #execute(CommandContext)}} async. To know more
   *     about asynchronization check <a
   *     href="https://bukkit.fandom.com/wiki/Scheduler_Programming">Bukkit wiki</a>
   */
  public BungeeCommand(
      String name,
      @NonNull List<BungeeCommand> children,
      @NonNull CommandManager manager,
      boolean async) {
    super(name);
    this.children = children;
    this.manager = manager;
    this.async = async;
  }

  /**
   * Create the command
   *
   * @param name the name of the command
   * @param permission the permission that the sender requires to execute the command {@link
   *     CommandSender#hasPermission(String)}
   * @param children the list of children commands which can be used with this parent prefix. Learn
   *     more in {@link me.googas.commands.annotations.Parent}
   * @param manager where the command will be registered used to get the {@link
   *     CommandManager#getMessagesProvider()} and {@link CommandManager#getProvidersRegistry()}
   * @param async Whether the command should {{@link #execute(CommandContext)}} async. To know more
   *     about asynchronization check <a
   *     href="https://bukkit.fandom.com/wiki/Scheduler_Programming">Bukkit wiki</a>
   * @param aliases the aliases which also allow to execute the command
   */
  public BungeeCommand(
      String name,
      String permission,
      @NonNull List<BungeeCommand> children,
      @NonNull CommandManager manager,
      boolean async,
      String... aliases) {
    super(name, permission, aliases);
    this.children = children;
    this.manager = manager;
    this.async = async;
  }

  /**
   * Checks if the command should be running async if so it will create the task with {@link
   * net.md_5.bungee.api.scheduler.TaskScheduler#runAsync(Plugin, Runnable)} this uses the {@link
   * CommandManager} plugin
   *
   * @param sender the executor of the command
   * @param args the arguments used in the command execution
   */
  public void runCheckSync(@NonNull CommandSender sender, @NonNull String[] args) {
    if (this.async) {
      ProxyServer.getInstance()
          .getScheduler()
          .runAsync(this.manager.getPlugin(), () -> run(sender, args));
    } else {
      run(sender, args);
    }
  }

  /**
   * This method does the command execution after {@link #runCheckSync(CommandSender, String[])}
   * finishes checking whether to run async or not.
   *
   * <p>This calls {@link #execute(CommandContext)} and the {@link Result} will be send to the
   * {@link CommandSender} if it is not null with {@link CommandSender#sendMessage(BaseComponent)}
   * and {@link Result} components
   *
   * @param sender the executor of the command
   * @param args the arguments used in the command execution
   */
  public void run(@NonNull CommandSender sender, @NonNull String[] args) {
    Result result =
        this.execute(
            new CommandContext(
                sender, args, manager.getMessagesProvider(), manager.getProvidersRegistry()));
    if (result != null) {
      for (BaseComponent component : result.getComponents()) {
        sender.sendMessage(component);
      }
    }
  }

  /**
   * Get the name of the children that have been added inside the command {@link #getChildren()}
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
    if (strings.length >= 1) {
      BungeeCommand command = this.getChildren(strings[0]);
      if (command != null) {
        command.execute(sender, Arrays.copyOfRange(strings, 1, strings.length));
      } else {
        this.runCheckSync(sender, strings);
      }
    } else {
      this.runCheckSync(sender, strings);
    }
  }

  @Override
  public abstract Result execute(@NonNull CommandContext context);

  @Override
  public boolean hasAlias(@NonNull String alias) {
    if (this.getName().equalsIgnoreCase(alias)) return true;
    for (String name : this.getAliases()) {
      if (name.equalsIgnoreCase(alias)) return true;
    }
    return false;
  }

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] strings) {
    if (strings.length == 1) {
      return Strings.copyPartials(strings[strings.length - 1], this.getChildrenNames());
    } else if (strings.length >= 2) {
      final BungeeCommand command = this.getChildren(strings[0]);
      if (command != null) {
        return command.onTabComplete(sender, Arrays.copyOfRange(strings, 1, strings.length));
      } else {
        return new ArrayList<>();
      }
    } else {
      return new ArrayList<>();
    }
  }
}

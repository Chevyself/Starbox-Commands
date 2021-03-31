package me.googas.commands.bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.EasyCommand;
import me.googas.commands.bukkit.context.CommandContext;
import me.googas.commands.bukkit.result.Result;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;

/**
 * This is the direct implementation of {@link EasyCommand} for the "Bukkit" module extending this
 * class allows to register commands in the {@link CommandManager} using {@link
 * CommandManager#register(EasyBukkitCommand)} the creation of a reflection command using {@link
 * CommandManager#parseCommands(Object)} returns a {@link AnnotatedCommand}
 *
 * <p>To parse {@link AnnotatedCommand} is required to use the annotation {@link
 * me.googas.commands.bukkit.annotations.Command} if you would like to create an extension the
 * method to override is {@link #execute(CommandContext)} but also you can override {@link
 * #tabComplete(CommandSender, String, String[])} at the moment the only tab complete will only
 * return the names of the children commands {@link #getChildren()}
 *
 * <p>This also allows to use the command asynchronously check {@link #EasyBukkitCommand(String,
 * String, String, List, boolean, CommandManager)} or {@link #EasyBukkitCommand(String, boolean,
 * CommandManager)}
 */
public abstract class EasyBukkitCommand extends Command
    implements EasyCommand<CommandContext, EasyBukkitCommand> {

  @NonNull @Getter protected final CommandManager manager;
  protected boolean async;

  /**
   * Create the command
   *
   * @param name the name of the command
   * @param async Whether the command should {{@link #execute(CommandContext)}} async. To know more
   *     about asynchronization check <a
   *     href="https://bukkit.fandom.com/wiki/Scheduler_Programming">Bukkit wiki</a>
   * @param manager where the command will be registered used to get the {@link
   *     CommandManager#getMessagesProvider()} and {@link CommandManager#getProvidersRegistry()}
   */
  public EasyBukkitCommand(@NonNull String name, boolean async, @NonNull CommandManager manager) {
    super(name);
    this.async = async;
    this.manager = manager;
  }

  /**
   * Create the command
   *
   * @param name the name of the command
   * @param description a simple description of the command
   * @param usageMessage a message describing how the message should executed. You can learn more
   *     about usage messages in {@link me.googas.commands.arguments.Argument}
   * @param aliases the aliases which also allow to execute the command
   * @param async Whether the command should {{@link #execute(CommandContext)}} async. To know more
   *     about asynchronization check <a
   *     href="https://bukkit.fandom.com/wiki/Scheduler_Programming">Bukkit wiki</a>
   * @param manager where the command will be registered used to get the {@link
   *     CommandManager#getMessagesProvider()} and {@link CommandManager#getProvidersRegistry()}
   */
  public EasyBukkitCommand(
      @NonNull String name,
      @NonNull String description,
      @NonNull String usageMessage,
      @NonNull List<String> aliases,
      boolean async,
      @NonNull CommandManager manager) {
    super(name, description, usageMessage, aliases);
    this.async = async;
    this.manager = manager;
  }

  /**
   * This method does the command execution after {@link #runCheckSync(CommandSender, String[])}
   * finishes checking whether to run async or not.
   *
   * <p>This calls {@link #execute(CommandContext)} and the {@link Result} will be send to the
   * {@link CommandSender} if it is not null with {@link CommandSender#sendMessage(String)} and
   * {@link Result} components
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
        sender.sendMessage(component.toLegacyText());
      }
    }
  }

  /**
   * Checks if the command should be running async if so it will create the task with {@link
   * org.bukkit.scheduler.BukkitScheduler#runTaskAsynchronously(Plugin, Runnable)} this uses the
   * {@link CommandManager} plugin
   *
   * @param sender the executor of the command
   * @param args the arguments used in the command execution
   */
  public void runCheckSync(@NonNull CommandSender sender, @NonNull String[] args) {
    if (this.async) {
      Bukkit.getScheduler().runTaskAsynchronously(manager.getPlugin(), () -> run(sender, args));
    } else {
      run(sender, args);
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
    for (EasyBukkitCommand child : this.getChildren()) {
      names.add(child.getName());
    }
    return names;
  }

  @Override
  public boolean execute(
      @NonNull CommandSender sender, @NonNull String alias, String @NonNull [] strings) {
    if (strings.length >= 1) {
      EasyBukkitCommand command = this.getChildren(strings[0]);
      if (command != null) {
        return command.execute(sender, alias, Arrays.copyOfRange(strings, 1, strings.length));
      } else {
        this.runCheckSync(sender, strings);
      }
    } else {
      this.runCheckSync(sender, strings);
    }
    return true;
  }

  @Override
  public @NonNull List<String> tabComplete(
      @NonNull CommandSender sender, @NonNull String alias, String @NonNull [] strings)
      throws IllegalArgumentException {
    if (strings.length == 1) {
      return StringUtil.copyPartialMatches(
          strings[strings.length - 1], this.getChildrenNames(), new ArrayList<>());
    } else if (strings.length >= 2) {
      final EasyBukkitCommand command = this.getChildren(strings[0]);
      if (command != null) {
        return command.tabComplete(sender, alias, Arrays.copyOfRange(strings, 1, strings.length));
      } else {
        return new ArrayList<>();
      }
    } else {
      return new ArrayList<>();
    }
  }

  @Override
  public abstract Result execute(@NonNull CommandContext context);
}

package me.googas.commands.bungee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import me.googas.commands.EasyCommand;
import me.googas.commands.bungee.context.CommandContext;
import me.googas.commands.bungee.result.Result;
import me.googas.commands.utility.Strings;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public abstract class BungeeCommand extends Command
    implements EasyCommand<CommandContext, BungeeCommand>, TabExecutor {

  @NonNull @Getter protected final CommandManager manager;
  @NonNull @Getter private final List<BungeeCommand> children;
  protected boolean async;

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
   * // TODO documentation this checks if it should run sync
   *
   * @param args
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

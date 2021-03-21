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
import org.bukkit.util.StringUtil;

/** TODO documentation */
public abstract class BukkitCommand extends Command
    implements EasyCommand<CommandContext, BukkitCommand> {

  @NonNull @Getter protected final CommandManager manager;
  protected boolean async;

  public BukkitCommand(String name, boolean async, @NonNull CommandManager manager) {
    super(name);
    this.async = async;
    this.manager = manager;
  }

  public BukkitCommand(
      String name,
      String description,
      String usageMessage,
      List<String> aliases,
      boolean async,
      @NonNull CommandManager manager) {
    super(name, description, usageMessage, aliases);
    this.async = async;
    this.manager = manager;
  }

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
   * // TODO documentation this checks if it should run sync
   *
   * @param args
   */
  public void runCheckSync(@NonNull CommandSender sender, @NonNull String[] args) {
    if (this.async) {
      Bukkit.getScheduler().runTaskAsynchronously(manager.getPlugin(), () -> run(sender, args));
    } else {
      run(sender, args);
    }
  }

  @NonNull
  public List<String> getChildrenNames() {
    List<String> names = new ArrayList<>();
    for (BukkitCommand child : this.getChildren()) {
      names.add(child.getName());
    }
    return names;
  }

  @Override
  public boolean execute(
      @NonNull CommandSender sender, @NonNull String alias, String @NonNull [] strings) {
    if (strings.length >= 1) {
      BukkitCommand command = this.getChildren(strings[0]);
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
      final BukkitCommand command = this.getChildren(strings[0]);
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

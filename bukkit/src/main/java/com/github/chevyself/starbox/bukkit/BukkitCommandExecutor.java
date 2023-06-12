package com.github.chevyself.starbox.bukkit;

import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.flags.CommandLineParser;
import java.util.List;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class BukkitCommandExecutor extends Command implements TabCompleter {

  @NonNull private final BukkitCommand command;
  private final boolean async;

  protected BukkitCommandExecutor(
      String name,
      String description,
      String usageMessage,
      List<String> aliases,
      @NonNull BukkitCommand command,
      boolean async) {
    super(name, description, usageMessage, aliases);
    this.command = command;
    this.async = async;
  }

  @Override
  public boolean execute(
      @NonNull CommandSender sender, @NonNull String label, @NonNull String[] strings) {
    CommandLineParser parser = CommandLineParser.parse(command.getOptions(), strings);
    CommandContext context =
        new CommandContext(
            parser, command, sender, command.getProvidersRegistry(), command.getMessagesProvider());
    if (this.async) {
      Bukkit.getScheduler()
          .runTaskAsynchronously(command.getAdapter().getPlugin(), () -> command.run(context));
    } else {
      command.run(context);
    }
    return true;
  }

  @Override
  public List<String> onTabComplete(
      @NonNull CommandSender sender,
      @NonNull Command command,
      @NonNull String name,
      @NonNull String[] args) {
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
}

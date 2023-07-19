package com.github.chevyself.starbox.bukkit;

import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.tab.BukkitTabCompleter;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BukkitCommandExecutor extends Command {

  @NonNull private static final BukkitTabCompleter reflectCompleter = new BukkitTabCompleter();

  @NonNull private static final BukkitTabCompleter tabCompleter = new BukkitTabCompleter();

  @NonNull @Getter private final BukkitCommand command;
  private final boolean async;

  public BukkitCommandExecutor(
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
  public List<String> tabComplete(
      @NonNull CommandSender sender, @NonNull String name, String @NonNull [] args)
      throws IllegalArgumentException {
    return BukkitCommandExecutor.tabCompleter.tabComplete(command, sender, name, args);
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
          .runTaskAsynchronously(command.getAdapter().getPlugin(), () -> command.execute(context));
    } else {
      command.execute(context);
    }
    return true;
  }
}

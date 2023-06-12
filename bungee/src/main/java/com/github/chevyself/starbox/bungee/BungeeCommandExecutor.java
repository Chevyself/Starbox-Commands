package com.github.chevyself.starbox.bungee;

import com.github.chevyself.starbox.bungee.commands.BungeeCommand;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.tab.BungeeReflectTabCompleter;
import com.github.chevyself.starbox.bungee.tab.BungeeTabCompleter;
import com.github.chevyself.starbox.commands.ReflectCommand;
import com.github.chevyself.starbox.common.Aliases;
import com.github.chevyself.starbox.parsers.CommandLineParser;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class BungeeCommandExecutor extends Command implements TabExecutor {

  @NonNull
  private static final BungeeReflectTabCompleter reflectCompleter = new BungeeReflectTabCompleter();

  @NonNull private static final BungeeTabCompleter genericCompleter = new BungeeTabCompleter();
  @NonNull private final BungeeCommand command;
  private final boolean async;

  public BungeeCommandExecutor(@NonNull BungeeCommand command, boolean async) {
    super(command.getName(), command.getPermission(), Aliases.getAliases(command));
    this.command = command;
    this.async = async;
  }

  @Override
  public void execute(CommandSender sender, String[] strings) {
    CommandLineParser parser = CommandLineParser.parse(command.getOptions(), strings);
    CommandContext context =
        new CommandContext(
            parser, command, sender, command.getProvidersRegistry(), command.getMessagesProvider());
    if (this.async) {
      ProxyServer.getInstance()
          .getScheduler()
          .runAsync(command.getAdapter().getPlugin(), () -> command.run(context));
    } else {
      command.run(context);
    }
  }

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] strings) {
    if (this.command instanceof ReflectCommand) {
      return BungeeCommandExecutor.reflectCompleter.tabComplete(
          this.command, sender, this.getName(), strings);
    } else {
      return BungeeCommandExecutor.genericCompleter.tabComplete(
          this.command, sender, this.getName(), strings);
    }
  }
}

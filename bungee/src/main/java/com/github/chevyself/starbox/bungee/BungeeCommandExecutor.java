package com.github.chevyself.starbox.bungee;

import com.github.chevyself.starbox.bungee.commands.BungeeCommand;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.common.Aliases;
import com.github.chevyself.starbox.flags.CommandLineParser;
import com.github.chevyself.starbox.util.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class BungeeCommandExecutor extends Command implements TabExecutor {

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
    if (this.getPermission() != null && !sender.hasPermission(this.getPermission())) {
      return new ArrayList<>();
    }
    if (strings.length == 1) {
      return Strings.copyPartials(strings[strings.length - 1], command.getChildrenNames());
    } else if (strings.length >= 2) {
      return command
          .getChildren(strings[0])
          .map(
              command ->
                  command
                      .getExecutor()
                      .onTabComplete(sender, Arrays.copyOfRange(strings, 1, strings.length)))
          .orElseGet(ArrayList::new);
    }
    return new ArrayList<>();
  }
}

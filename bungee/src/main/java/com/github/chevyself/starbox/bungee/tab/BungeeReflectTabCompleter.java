package com.github.chevyself.starbox.bungee.tab;

import com.github.chevyself.starbox.bungee.commands.BungeeCommand;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.common.tab.ReflectTabCompleter;
import com.github.chevyself.starbox.flags.CommandLineParser;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;

public class BungeeReflectTabCompleter
    extends ReflectTabCompleter<CommandContext, BungeeCommand, CommandSender> {

  @Override
  public String getPermission(@NonNull BungeeCommand command) {
    return command.getPermission();
  }

  @Override
  public boolean hasPermission(@NonNull CommandSender sender, @NonNull String permission) {
    return sender.hasPermission(permission);
  }

  @Override
  public @NonNull CommandContext createContext(
      @NonNull CommandLineParser parser,
      @NonNull BungeeCommand command,
      @NonNull CommandSender sender) {
    return new CommandContext(
        parser, command, sender, command.getProvidersRegistry(), command.getMessagesProvider());
  }
}

package com.github.chevyself.starbox.bungee.tab;

import com.github.chevyself.starbox.bungee.commands.BungeeCommand;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.common.tab.GenericTabCompleter;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;

public class BungeeTabCompleter
    extends GenericTabCompleter<CommandContext, BungeeCommand, CommandSender> {

  @Override
  public String getPermission(@NonNull BungeeCommand command) {
    return command.getPermission();
  }

  @Override
  public boolean hasPermission(@NonNull CommandSender sender, @NonNull String permission) {
    return sender.hasPermission(permission);
  }
}

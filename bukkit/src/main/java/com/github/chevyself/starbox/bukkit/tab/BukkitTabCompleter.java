package com.github.chevyself.starbox.bukkit.tab;

import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.common.tab.GenericTabCompleter;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

public class BukkitTabCompleter
    extends GenericTabCompleter<CommandContext, BukkitCommand, CommandSender> {

  @Override
  public String getPermission(@NonNull BukkitCommand command) {
    return command.getPermission();
  }

  @Override
  public boolean hasPermission(@NonNull CommandSender sender, @NonNull String permission) {
    return sender.hasPermission(permission);
  }
}

package com.github.chevyself.starbox.velocity.tab;

import com.github.chevyself.starbox.common.tab.GenericTabCompleter;
import com.github.chevyself.starbox.velocity.commands.VelocityCommand;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import com.velocitypowered.api.command.CommandSource;
import lombok.NonNull;

public class VelocityTabCompleter
    extends GenericTabCompleter<CommandContext, VelocityCommand, CommandSource> {

  @Override
  public String getPermission(@NonNull VelocityCommand command) {
    return command.getPermission();
  }

  @Override
  public boolean hasPermission(@NonNull CommandSource sender, @NonNull String permission) {
    return sender.hasPermission(permission);
  }
}

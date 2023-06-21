package com.github.chevyself.starbox.velocity.commands;

import com.github.chevyself.starbox.commands.StarboxCommand;
import com.github.chevyself.starbox.velocity.VelocityCommandExecutor;
import com.github.chevyself.starbox.velocity.context.CommandContext;
import lombok.NonNull;

public interface VelocityCommand extends StarboxCommand<CommandContext, VelocityCommand> {

  @NonNull
  VelocityCommandExecutor getExecutor();

  String getPermission();

  boolean isAsync();
}

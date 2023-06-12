package com.github.chevyself.starbox.bungee.commands;

import com.github.chevyself.starbox.bungee.BungeeAdapter;
import com.github.chevyself.starbox.bungee.BungeeCommandExecutor;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.commands.StarboxCommand;
import lombok.NonNull;

public interface BungeeCommand extends StarboxCommand<CommandContext, BungeeCommand> {

  @NonNull
  BungeeCommandExecutor getExecutor();

  @NonNull
  BungeeAdapter getAdapter();

  String getPermission();
}

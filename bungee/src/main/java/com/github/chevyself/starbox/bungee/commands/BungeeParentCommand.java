package com.github.chevyself.starbox.bungee.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.bungee.BungeeAdapter;
import com.github.chevyself.starbox.bungee.BungeeCommandExecutor;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.commands.AbstractParentCommand;
import lombok.Getter;
import lombok.NonNull;

public class BungeeParentCommand extends AbstractParentCommand<CommandContext, BungeeCommand>
    implements BungeeCommand {

  @NonNull @Getter private final BungeeCommandExecutor executor;
  @NonNull @Getter private final BungeeAdapter adapter;
  @Getter private final String permission;

  public BungeeParentCommand(
      @NonNull CommandManager<CommandContext, BungeeCommand> commandManager,
      @NonNull Command annotation,
      @NonNull BungeeAdapter adapter,
      String permission,
      boolean async) {
    super(commandManager, annotation);
    this.executor = new BungeeCommandExecutor(this, async);
    this.adapter = adapter;
    this.permission = permission;
  }
}

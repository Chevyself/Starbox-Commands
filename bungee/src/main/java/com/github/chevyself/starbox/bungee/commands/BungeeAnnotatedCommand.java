package com.github.chevyself.starbox.bungee.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.bungee.BungeeAdapter;
import com.github.chevyself.starbox.bungee.BungeeCommandExecutor;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.commands.AbstractAnnotatedCommand;
import java.lang.reflect.Method;
import lombok.Getter;
import lombok.NonNull;

public class BungeeAnnotatedCommand extends AbstractAnnotatedCommand<CommandContext, BungeeCommand>
    implements BungeeCommand {

  @NonNull @Getter private final BungeeAdapter adapter;
  @NonNull @Getter private final BungeeCommandExecutor executor;
  @Getter private final String permission;

  public BungeeAnnotatedCommand(
      @NonNull CommandManager<CommandContext, BungeeCommand> commandManager,
      @NonNull Command annotation,
      @NonNull Object object,
      @NonNull Method method,
      @NonNull BungeeAdapter adapter,
      String permission,
      boolean async) {
    super(commandManager, annotation, object, method);
    this.adapter = adapter;
    this.executor = new BungeeCommandExecutor(this, async);
    this.permission = permission;
  }
}

package com.github.chevyself.starbox.bukkit.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.bukkit.BukkitAdapter;
import com.github.chevyself.starbox.bukkit.BukkitCommandExecutor;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.commands.AbstractAnnotatedCommand;
import com.github.chevyself.starbox.common.Async;
import java.lang.reflect.Method;
import lombok.Getter;
import lombok.NonNull;

public class BukkitAnnotatedCommand extends AbstractAnnotatedCommand<CommandContext, BukkitCommand>
    implements BukkitCommand {

  @NonNull @Getter private final BukkitAdapter adapter;
  @NonNull @Getter private final BukkitCommandExecutor executor;
  @Getter private final String permission;

  public BukkitAnnotatedCommand(
      @NonNull CommandManager<CommandContext, BukkitCommand> commandManager,
      @NonNull Command annotation,
      @NonNull Object object,
      @NonNull Method method,
      @NonNull BukkitAdapter adapter,
      String permission) {
    super(commandManager, annotation, object, method);
    this.adapter = adapter;
    this.permission = permission;
    this.executor =
        new BukkitCommandExecutor(
            this.getName(),
            Command.Supplier.getDescription(method),
            Command.Supplier.getUsage(method, this.getArguments(), annotation.aliases()),
            this.getAliases(),
            this,
            method.isAnnotationPresent(Async.class));
  }
}

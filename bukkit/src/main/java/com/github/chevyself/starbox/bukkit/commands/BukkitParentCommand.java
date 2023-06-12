package com.github.chevyself.starbox.bukkit.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.bukkit.BukkitAdapter;
import com.github.chevyself.starbox.bukkit.BukkitCommandExecutor;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.commands.AbstractParentCommand;
import lombok.Getter;
import lombok.NonNull;

public class BukkitParentCommand extends AbstractParentCommand<CommandContext, BukkitCommand>
    implements BukkitCommand {

  @NonNull @Getter private final BukkitAdapter adapter;
  @NonNull @Getter private final BukkitCommandExecutor executor;
  @Getter private final String permission;

  public BukkitParentCommand(
      @NonNull CommandManager<CommandContext, BukkitCommand> commandManager,
      @NonNull Command annotation,
      @NonNull BukkitAdapter adapter,
      String permission,
      String description,
      String usage,
      boolean async) {
    super(commandManager, annotation);
    this.adapter = adapter;
    this.permission = permission;
    this.executor =
        new BukkitCommandExecutor(
            this.getName(), description, usage, this.getAliases(), this, async);
  }
}

package com.github.chevyself.starbox.bukkit;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.bukkit.commands.BukkitAnnotatedCommand;
import com.github.chevyself.starbox.bukkit.commands.BukkitCommand;
import com.github.chevyself.starbox.bukkit.commands.BukkitParentCommand;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.common.Async;
import com.github.chevyself.starbox.common.CommandPermission;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.parsers.ParentCommandSupplier;
import java.lang.reflect.Method;
import java.util.ArrayList;
import lombok.NonNull;

public class BukkitCommandParser extends CommandParser<CommandContext, BukkitCommand> {

  public BukkitCommandParser(
      @NonNull BukkitAdapter adapter,
      @NonNull CommandManager<CommandContext, BukkitCommand> commandManager) {
    super(adapter, commandManager);
  }

  @Override
  public @NonNull ParentCommandSupplier<CommandContext, BukkitCommand> getParentCommandSupplier() {
    return (annotation, clazz) ->
        new BukkitParentCommand(
            this.commandManager,
            annotation,
            (BukkitAdapter) this.adapter,
            CommandPermission.Supplier.getPermission(clazz),
            Command.Supplier.getDescription(clazz),
            Command.Supplier.getUsage(clazz, new ArrayList<>(), annotation.aliases()),
            clazz.isAnnotationPresent(Async.class));
  }

  @Override
  public @NonNull BukkitCommand parseCommand(
      @NonNull Object object, @NonNull Method method, @NonNull Command annotation) {
    return new BukkitAnnotatedCommand(
        this.commandManager,
        annotation,
        object,
        method,
        (BukkitAdapter) adapter,
        CommandPermission.Supplier.getPermission(method));
  }
}

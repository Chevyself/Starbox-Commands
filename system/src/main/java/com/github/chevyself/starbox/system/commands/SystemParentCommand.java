package com.github.chevyself.starbox.system.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.commands.AbstractParentCommand;
import com.github.chevyself.starbox.system.context.CommandContext;
import lombok.NonNull;

public class SystemParentCommand extends AbstractParentCommand<CommandContext, SystemCommand> implements SystemCommand {


  public SystemParentCommand(@NonNull Command annotation,
      @NonNull CommandManager<CommandContext, SystemCommand> commandManager) {
    super(annotation, commandManager);
  }
}

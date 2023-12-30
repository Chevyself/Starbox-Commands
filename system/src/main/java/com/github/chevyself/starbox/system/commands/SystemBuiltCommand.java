package com.github.chevyself.starbox.system.commands;

import com.github.chevyself.starbox.commands.AbstractBuiltCommand;
import com.github.chevyself.starbox.commands.CommandBuilder;
import com.github.chevyself.starbox.system.context.CommandContext;
import lombok.NonNull;

public class SystemBuiltCommand extends AbstractBuiltCommand<CommandContext, SystemCommand>
    implements SystemCommand {
  public SystemBuiltCommand(@NonNull CommandBuilder<CommandContext, SystemCommand> builder) {
    super(builder);
  }
}

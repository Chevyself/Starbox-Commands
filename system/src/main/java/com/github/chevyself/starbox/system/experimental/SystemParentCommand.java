package com.github.chevyself.starbox.system.experimental;

import com.github.chevyself.starbox.experimental.AbstractParentCommand;
import com.github.chevyself.starbox.system.context.CommandContext;
import lombok.NonNull;

public class SystemParentCommand extends AbstractParentCommand<CommandContext, SystemCommand>
    implements SystemCommand {

  public SystemParentCommand(@NonNull String... aliases) {
    super(aliases);
  }
}

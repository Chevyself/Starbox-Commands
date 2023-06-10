package com.github.chevyself.starbox.system.experimental;

import com.github.chevyself.starbox.experimental.AbstractAnnotatedCommand;
import com.github.chevyself.starbox.experimental.Command;
import com.github.chevyself.starbox.experimental.CommandManager;
import com.github.chevyself.starbox.system.context.CommandContext;
import java.lang.reflect.Method;
import lombok.NonNull;

public class SystemAnnotatedCommand extends AbstractAnnotatedCommand<CommandContext, SystemCommand>
    implements SystemCommand {

  protected SystemAnnotatedCommand(
      @NonNull Command annotation,
      @NonNull Object object,
      @NonNull Method method,
      @NonNull CommandManager<CommandContext, SystemCommand> commandManager) {
    super(annotation, object, method, commandManager);
  }
}

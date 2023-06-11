package com.github.chevyself.starbox.system.commands;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.commands.AbstractAnnotatedCommand;
import com.github.chevyself.starbox.system.context.CommandContext;
import java.lang.reflect.Method;
import lombok.NonNull;

public class AnnotatedSystemCommand extends AbstractAnnotatedCommand<CommandContext, SystemCommand>
    implements SystemCommand {

  public AnnotatedSystemCommand(
      @NonNull Command annotation,
      @NonNull Object object,
      @NonNull Method method,
      @NonNull CommandManager<CommandContext, SystemCommand> commandManager) {
    super(annotation, object, method, commandManager);
  }
}

package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.CommandManager;
import com.github.chevyself.starbox.annotations.Command;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.system.commands.AnnotatedSystemCommand;
import com.github.chevyself.starbox.system.commands.SystemCommand;
import com.github.chevyself.starbox.system.commands.SystemParentCommand;
import com.github.chevyself.starbox.system.context.CommandContext;
import java.lang.reflect.Method;
import java.util.function.Function;
import lombok.NonNull;

public class SystemCommandParser extends CommandParser<CommandContext, SystemCommand> {

  public SystemCommandParser(
      @NonNull CommandManager<CommandContext, SystemCommand> commandManager) {
    super(commandManager);
  }

  @Override
  public @NonNull Function<Command, SystemCommand> getParentCommandSupplier() {
    return annotation -> new SystemParentCommand(annotation, commandManager);
  }

  @Override
  public @NonNull SystemCommand parseCommand(
      @NonNull Object object, @NonNull Method method, @NonNull Command annotation) {
    return new AnnotatedSystemCommand(annotation, object, method, commandManager);
  }
}

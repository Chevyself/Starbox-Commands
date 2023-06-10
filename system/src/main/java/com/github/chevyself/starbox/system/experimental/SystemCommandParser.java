package com.github.chevyself.starbox.system.experimental;

import com.github.chevyself.starbox.experimental.Command;
import com.github.chevyself.starbox.experimental.CommandManager;
import com.github.chevyself.starbox.experimental.CommandParser;
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
    return command -> {
      return new SystemParentCommand(command.aliases());
    };
  }

  @Override
  public void checkReturnType(@NonNull Method method) {
    // TODO check either void or result
  }

  @Override
  public @NonNull SystemCommand parseCommand(
      @NonNull Object object, @NonNull Method method, @NonNull Command annotation) {
    return new SystemAnnotatedCommand(annotation, object, method, this.commandManager);
  }
}

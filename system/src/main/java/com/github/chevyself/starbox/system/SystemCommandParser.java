package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.StarboxCommandManager;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.system.context.CommandContext;
import com.github.chevyself.starbox.time.TimeUtil;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import lombok.Getter;
import lombok.NonNull;

/** Command parsing implementation for the system. */
public class SystemCommandParser implements CommandParser<Command, CommandContext, SystemCommand> {

  @NonNull @Getter private final CommandManager commandManager;

  /**
   * Create the command parser.
   *
   * @param commandManager the command manager for system commands
   */
  public SystemCommandParser(@NonNull CommandManager commandManager) {
    this.commandManager = commandManager;
  }

  @Override
  public @NonNull Class<Command> getAnnotationClass() {
    return Command.class;
  }

  @Override
  public @NonNull Function<Command, SystemCommand> getParentCommandSupplier() {
    return command -> {
      Duration duration = TimeUtil.durationOf(command.cooldown());
      return new AbstractSystemCommand(
          Arrays.asList(command.aliases()),
          new ArrayList<>(),
          Option.of(command.options()),
          this.getMiddlewares(command),
          duration.isZero() ? null : new CooldownManager(duration)) {
        @Override
        public SystemResult run(@NonNull CommandContext context) {
          return new Result(
              "usage: "
                  + this.getName()
                  + " "
                  + commandManager.getMessagesProvider().commandHelp(this, context));
        }
      };
    };
  }

  @NonNull
  private List<Middleware<CommandContext>> getMiddlewares(@NonNull Command command) {
    return StarboxCommandManager.getMiddlewares(
        commandManager.getGlobalMiddlewares(),
        commandManager.getMiddlewares(),
        command.include(),
        command.exclude());
  }

  @Override
  public void checkReturnType(@NonNull Method method) {}

  @Override
  public SystemCommand parseCommand(
      @NonNull Object object, @NonNull Method method, @NonNull Command annotation) {
    Duration duration = TimeUtil.durationOf(annotation.cooldown());
    return new ReflectSystemCommand(
        commandManager,
        Arrays.asList(annotation.aliases()),
        Option.of(annotation.options()),
        this.getMiddlewares(annotation),
        method,
        object,
        Argument.parseArguments(method),
        new ArrayList<>(),
        !duration.isZero() ? new CooldownManager(duration) : null);
  }
}

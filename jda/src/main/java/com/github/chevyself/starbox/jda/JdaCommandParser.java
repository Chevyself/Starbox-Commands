package com.github.chevyself.starbox.jda;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.StarboxCommandManager;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.jda.annotations.Command;
import com.github.chevyself.starbox.jda.annotations.Entry;
import com.github.chevyself.starbox.jda.context.CommandContext;
import com.github.chevyself.starbox.jda.cooldown.CooldownManager;
import com.github.chevyself.starbox.jda.result.JdaResult;
import com.github.chevyself.starbox.jda.result.Result;
import com.github.chevyself.starbox.jda.result.ResultType;
import com.github.chevyself.starbox.parsers.CommandParser;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;
import lombok.NonNull;

public class JdaCommandParser implements CommandParser<Command, CommandContext, JdaCommand> {

  @NonNull @Getter private final CommandManager commandManager;

  public JdaCommandParser(@NonNull CommandManager commandManager) {
    this.commandManager = commandManager;
  }

  @Override
  public @NonNull Class<Command> getAnnotationClass() {
    return Command.class;
  }

  @Override
  public @NonNull Function<Command, JdaCommand> getParentCommandSupplier() {
    return command -> {
      List<String> aliases = new ArrayList<>(Arrays.asList(command.aliases()));
      List<JdaCommand> children = new ArrayList<>();
      return new JdaCommand(
          commandManager,
          command.description(),
          this.getMap(command),
          Option.of(command.options()),
          this.getMiddlewares(command),
          CooldownManager.of(command).orElse(null)) {
        @Override
        public @NonNull List<String> getAliases() {
          return aliases;
        }

        @Override
        JdaResult run(@NonNull CommandContext context) {
          return Result.forType(ResultType.USAGE)
              .setDescription(commandManager.getMessagesProvider().commandHelp(this, context))
              .build();
        }

        @Override
        public @NonNull Collection<JdaCommand> getChildren() {
          return children;
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

  @NonNull
  private Map<String, String> getMap(@NonNull Command annotation) {
    Map<String, String> map = new HashMap<>();
    for (Entry entry : annotation.map()) {
      map.put(entry.key(), entry.value());
    }
    return map;
  }

  @Override
  public void checkReturnType(@NonNull Method method) {
    if (!JdaResult.class.isAssignableFrom(method.getReturnType())
        && !method.getReturnType().equals(Void.TYPE)) {
      throw new IllegalArgumentException(method + " must return void or " + JdaResult.class);
    }
  }

  @Override
  public JdaCommand parseCommand(
      @NonNull Object object, @NonNull Method method, @NonNull Command annotation) {
    return new AnnotatedCommand(
        commandManager,
        annotation.description(),
        this.getMap(annotation),
        Option.of(annotation.options()),
        this.getMiddlewares(annotation),
        CooldownManager.of(annotation).orElse(null),
        Arrays.asList(annotation.aliases()),
        method,
        object,
        Argument.parseArguments(method));
  }
}

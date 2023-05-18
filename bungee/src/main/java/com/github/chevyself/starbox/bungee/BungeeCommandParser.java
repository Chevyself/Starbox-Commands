package com.github.chevyself.starbox.bungee;

import com.github.chevyself.starbox.CommandParser;
import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.StarboxCommandManager;
import com.github.chevyself.starbox.bungee.annotations.Command;
import com.github.chevyself.starbox.bungee.context.CommandContext;
import com.github.chevyself.starbox.bungee.result.BungeeResult;
import com.github.chevyself.starbox.bungee.result.Result;
import com.github.chevyself.starbox.exceptions.CommandRegistrationException;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.util.ClassFinder;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import lombok.Getter;
import lombok.NonNull;

public class BungeeCommandParser implements CommandParser<Command, CommandContext, BungeeCommand> {

  @NonNull @Getter private final CommandManager commandManager;

  public BungeeCommandParser(@NonNull CommandManager commandManager) {
    this.commandManager = commandManager;
  }

  @Override
  public @NonNull Class<Command> getAnnotationClass() {
    return Command.class;
  }

  @Override
  public @NonNull Function<Command, BungeeCommand> getParentCommandSupplier() {
    return command ->
        new BungeeCommand(
            command.aliases()[0],
            command.permission().isEmpty() ? null : command.permission(),
            new ArrayList<>(),
            commandManager,
            Option.of(command.options()),
            this.getMiddlewares(command),
            command.async(),
            CooldownManager.of(command.cooldown()).orElse(null),
            Arrays.copyOfRange(command.aliases(), 1, command.aliases().length)) {
          @Override
          public BungeeResult execute(@NonNull CommandContext context) {
            return Result.of(commandManager.getMessagesProvider().commandHelp(this, context));
          }
        };
  }

  @Override
  public @NonNull ClassFinder<?> createClassFinder(@NonNull String packageName) {
    return CommandParser.super
        .createClassFinder(packageName)
        .setClassLoaderSupplier(() -> commandManager.getPlugin().getClass().getClassLoader());
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
  public void checkReturnType(@NonNull Method method) {
    if (!BungeeResult.class.isAssignableFrom(method.getReturnType())
        && !method.getReturnType().equals(Void.TYPE)) {
      throw new CommandRegistrationException(method + " must return void or " + BungeeResult.class);
    }
  }

  @Override
  public BungeeCommand parseCommand(
      @NonNull Object object, @NonNull Method method, @NonNull Command annotation) {
    return null;
  }
}

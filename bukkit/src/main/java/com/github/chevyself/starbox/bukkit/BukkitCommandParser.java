package com.github.chevyself.starbox.bukkit;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.StarboxCommandManager;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.bukkit.annotations.Command;
import com.github.chevyself.starbox.bukkit.context.CommandContext;
import com.github.chevyself.starbox.bukkit.result.BukkitResult;
import com.github.chevyself.starbox.bukkit.result.Result;
import com.github.chevyself.starbox.exceptions.CommandRegistrationException;
import com.github.chevyself.starbox.flags.Option;
import com.github.chevyself.starbox.parsers.CommandParser;
import com.github.chevyself.starbox.util.ClassFinder;
import com.github.chevyself.starbox.util.Strings;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import lombok.Getter;
import lombok.NonNull;

/** Command parsing implementation for bukkit. */
public class BukkitCommandParser
    implements CommandParser<Command, CommandContext, StarboxBukkitCommand> {

  @NonNull @Getter private final CommandManager commandManager;

  /**
   * Create the command parser.
   *
   * @param commandManager the command manager for bukkit commands
   */
  public BukkitCommandParser(@NonNull CommandManager commandManager) {
    this.commandManager = commandManager;
  }

  @Override
  public @NonNull Class<Command> getAnnotationClass() {
    return Command.class;
  }

  @Override
  public @NonNull <O> ClassFinder<O> createClassFinder(
      Class<O> clazz, @NonNull String packageName) {
    return CommandParser.super
        .createClassFinder(clazz, packageName)
        .setClassLoaderSupplier(() -> commandManager.getPlugin().getClass().getClassLoader());
  }

  @Override
  public @NonNull Function<Command, StarboxBukkitCommand> getParentCommandSupplier() {
    List<StarboxBukkitCommand> children = new ArrayList<>();
    return (command) ->
        new StarboxBukkitCommand(
            commandManager,
            command.aliases()[0],
            command.aliases().length > 1
                ? Arrays.asList(Arrays.copyOfRange(command.aliases(), 1, command.aliases().length))
                : new ArrayList<>(),
            command.description(),
            "/" + Strings.buildUsageAliases(command.aliases()) + " <command> [arguments]",
            Option.of(command.options()),
            this.getMiddlewares(command),
            command.async(),
            CooldownManager.of(command.cooldown()).orElse(null)) {
          @Override
          public BukkitResult execute(@NonNull CommandContext context) {
            return Result.of(commandManager.getMessagesProvider().commandHelp(this, context));
          }

          @Override
          public String getPermission() {
            return command.permission();
          }

          @Override
          public boolean hasAlias(@NonNull String alias) {
            for (String string : command.aliases()) {
              if (alias.equalsIgnoreCase(string)) {
                return true;
              }
            }
            return false;
          }

          public @NonNull Collection<StarboxBukkitCommand> getChild() {
            return children;
          }
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
  public void checkReturnType(@NonNull Method method) {
    if (!BukkitResult.class.isAssignableFrom(method.getReturnType())
        && !method.getReturnType().equals(Void.TYPE)) {
      throw new CommandRegistrationException(method + " must return void or " + BukkitResult.class);
    }
  }

  @Override
  public StarboxBukkitCommand parseCommand(
      @NonNull Object object, @NonNull Method method, @NonNull Command command) {
    List<Argument<?>> arguments =
        Argument.parseArguments(method.getParameterTypes(), method.getParameterAnnotations());
    return new AnnotatedCommand(
        commandManager,
        command.aliases()[0],
        command.aliases().length > 1
            ? Arrays.asList(Arrays.copyOfRange(command.aliases(), 1, command.aliases().length))
            : new ArrayList<>(),
        command.permission(),
        command.description(),
        "/"
            + Strings.buildUsageAliases(command.aliases())
            + " "
            + Argument.generateUsage(arguments),
        Option.of(command.options()),
        this.getMiddlewares(command),
        command.async(),
        CooldownManager.of(command.cooldown()).orElse(null),
        method,
        object,
        arguments,
        new ArrayList<>());
  }
}

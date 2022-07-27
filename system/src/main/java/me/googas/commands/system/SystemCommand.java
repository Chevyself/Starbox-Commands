package me.googas.commands.system;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import me.googas.commands.Middleware;
import me.googas.commands.StarboxCommand;
import me.googas.commands.system.context.CommandContext;
import me.googas.commands.util.Strings;

/**
 * This is the direct implementation of {@link StarboxCommand} for the "System" module extending
 * this class allows to parseAndRegister commands in the {@link CommandManager} using {@link
 * CommandManager#register(SystemCommand)} the creation of a reflection command using {@link
 * CommandManager#parseCommands(Object)} returns a {@link ReflectSystemCommand}
 *
 * <p>To parse {@link ReflectSystemCommand} is required to use the annotation {@link
 * me.googas.commands.system.Command} if you would like to create an extension the method to
 * override is {@link #run(CommandContext)}
 */
public interface SystemCommand extends StarboxCommand<CommandContext, SystemCommand> {

  /**
   * Execute the command. This will run after {@link #execute(CommandContext)} does not find any
   * children to execute
   *
   * @param context the context to run the command
   * @return the result of the command execution if it has a message it will be printed
   * @see SystemResult
   */
  SystemResult run(@NonNull CommandContext context);

  /**
   * Get how the {@link me.googas.commands.system.context.sender.CommandSender} may input the
   * arguments to successfully run the command
   *
   * @return the usage of the command in case it is a {@link ReflectSystemCommand} it will be
   *     auto-generated using {@link me.googas.commands.util.Strings#buildUsageAliases(String...)}
   *     and {@link me.googas.commands.arguments.Argument#generateUsage(List)}
   */
  @NonNull
  String getUsage();

  @Override
  default SystemResult execute(@NonNull CommandContext context) {
    @NonNull String[] strings = context.getStrings();
    if (strings.length >= 1) {
      Optional<SystemCommand> optionalCommand = this.getChildren(strings[0]);
      if (optionalCommand.isPresent()) {
        String[] copy = Arrays.copyOfRange(strings, 1, strings.length);
        return optionalCommand
            .get()
            .execute(
                new CommandContext(
                    this,
                    context.getSender(),
                    copy,
                    Strings.join(copy),
                    context.getRegistry(),
                    context.getMessagesProvider(),
                    context.getFlags()));
      }
    }
    SystemResult result =
        this.getMiddlewares().stream()
            .map(middleware -> middleware.next(context))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .map(
                starboxResult -> {
                  // Here maybe thrown an error because the wrong result was provided
                  return starboxResult instanceof SystemResult
                      ? (SystemResult) starboxResult
                      : null;
                })
            .orElseGet(() -> this.run(context));
    this.getMiddlewares().forEach(middleware -> middleware.next(context, result));
    return result;
  }

  @Override
  @NonNull
  Collection<Middleware<CommandContext>> getMiddlewares();

  @Override
  @NonNull
  Optional<CooldownManager> getCooldownManager();
}

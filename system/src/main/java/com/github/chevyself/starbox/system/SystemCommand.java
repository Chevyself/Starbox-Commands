package com.github.chevyself.starbox.system;

import com.github.chevyself.starbox.Middleware;
import com.github.chevyself.starbox.StarboxCommand;
import com.github.chevyself.starbox.arguments.Argument;
import com.github.chevyself.starbox.flags.FlagArgument;
import com.github.chevyself.starbox.system.context.CommandContext;
import com.github.chevyself.starbox.system.context.sender.CommandSender;
import com.github.chevyself.starbox.util.Strings;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;

/**
 * This is the direct implementation of {@link StarboxCommand} for the "System" module extending
 * this class allows to parseAndRegister commands in the {@link CommandManager} using {@link
 * CommandManager#register(SystemCommand)} the creation of a reflection command using {@link
 * CommandManager#parseCommands(Object)} returns a {@link ReflectSystemCommand}
 *
 * <p>To parse {@link ReflectSystemCommand} is required to use the annotation {@link Command} if you
 * would like to create an extension the method to override is {@link #run(CommandContext)}
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
   * Get how the {@link CommandSender} may input the arguments to successfully run the command
   *
   * @return the usage of the command in case it is a {@link ReflectSystemCommand} it will be
   *     auto-generated using {@link Strings#buildUsageAliases(String...)} and {@link
   *     Argument#generateUsage(List)}
   */
  @NonNull
  String getUsage();

  @Override
  default SystemResult execute(@NonNull CommandContext context) {
    @NonNull String[] strings = context.getStrings();
    if (strings.length >= 1) {
      Optional<SystemCommand> optionalCommand = this.getChildren(strings[0]);
      if (optionalCommand.isPresent()) {
        SystemCommand subcommand = optionalCommand.get();
        String[] copy = Arrays.copyOfRange(strings, 1, strings.length);
        FlagArgument.Parser parse = FlagArgument.parse(subcommand.getOptions(), copy);
        // FIXME: Much check in other modules that this is correct, this was not
        // parsing flags correctly. It's been fixed in this module.
        return subcommand.execute(
            new CommandContext(
                this,
                context.getSender(),
                parse.getArgumentsArray(),
                parse.getArgumentsString(),
                context.getRegistry(),
                context.getMessagesProvider(),
                parse.getFlags()));
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

  @NonNull
  String getName();
}

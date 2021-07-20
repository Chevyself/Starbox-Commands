package me.googas.commands.system;

import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import me.googas.commands.StarboxCommand;
import me.googas.commands.system.context.CommandContext;

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
   * @see Result
   * @param context the context to run the command
   * @return the result of the command execution if it has a message it will be printed
   */
  Result run(@NonNull CommandContext context);

  /**
   * Get how the {@link me.googas.commands.system.context.sender.CommandSender} may input the
   * arguments to successfully run the command
   *
   * @return the usage of the command in case it is a {@link ReflectSystemCommand} it will be
   *     auto-generated using {@link me.googas.starbox.Strings#buildUsageAliases(String...)} and
   *     {@link me.googas.commands.arguments.Argument#generateUsage(List)}
   */
  @NonNull
  String getUsage();

  @Override
  default Result execute(@NonNull CommandContext context) {
    @NonNull String[] strings = context.getStrings();
    if (strings.length >= 1) {
      SystemCommand command = this.getChildren(strings[0]);
      if (command != null) {
        return command.execute(
            new CommandContext(
                context.getSender(),
                Arrays.copyOfRange(strings, 1, strings.length),
                context.getRegistry(),
                context.getMessagesProvider()));
      }
    }
    return this.run(context);
  }
}

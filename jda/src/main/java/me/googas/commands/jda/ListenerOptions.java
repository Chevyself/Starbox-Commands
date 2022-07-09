package me.googas.commands.jda;

import lombok.NonNull;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.result.JdaResult;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * The listener options allow to change the output of the {@link
 * me.googas.commands.jda.listener.CommandListener} to Discord.
 *
 * <p>For example:
 *
 * <ul>
 *   <li>Deleting the usage command
 *   <li>Deleting the result command
 *   <li>Embedding messages
 * </ul>
 *
 * <p>The direct implementation for this class is {@link GenericListenerOptions} which allows the
 * customization described above but if you create an implementation of this class you might be able
 * to customize it even further
 */
public interface ListenerOptions {

  /**
   * Called before the command is executed. This means before {@link
   * JdaCommand#execute(CommandContext)}
   *
   * @param event the event that is causing a command to be executed
   * @param name the alias of the {@link JdaCommand} that is going to be executed
   * @param strings the arguments used to execute the command
   */
  void preCommand(
      @NonNull SlashCommandInteractionEvent event, @NonNull String name, @NonNull String[] strings);

  /**
   * Called before the command is executed. This means before {@link
   * JdaCommand#execute(CommandContext)}
   *
   * @param event the event that is causing a command to be executed
   * @param commandName the alias of the {@link JdaCommand} that is going to be executed
   * @param strings the arguments used to execute the command
   */
  void preCommand(
      @NonNull MessageReceivedEvent event, @NonNull String commandName, @NonNull String[] strings);

  /**
   * Handles any kind of error that is thrown in {@link
   * me.googas.commands.jda.listener.CommandListener}
   *
   * @param fail any kind of exception thrown in {@link
   *     me.googas.commands.jda.listener.CommandListener}
   * @param context the context of the command execution
   */
  void handle(@NonNull Throwable fail, @NonNull CommandContext context);

  /**
   * Get the prefix that is used for commands inside a {@link Guild}. If the parameter {@link Guild}
   * is null means that the command was not executed inside of one meaning that the message was sent
   * in private messages
   *
   * @param guild the guild where the command was executed or null if it was not executed in one
   * @return the prefix that runs the commands inside the guild
   */
  @NonNull
  String getPrefix(Guild guild);

  /**
   * Handle the result from a command execution.
   *
   * @param result the result to handle
   * @param context the context which executed the command
   */
  void handle(JdaResult result, @NonNull CommandContext context);
}

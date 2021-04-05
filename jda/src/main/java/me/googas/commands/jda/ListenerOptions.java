package me.googas.commands.jda;

import java.awt.*;
import java.util.function.Consumer;
import lombok.NonNull;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.result.Result;
import net.dv8tion.jda.api.entities.Message;
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
 * The direct implementation for this class is {@link DefaultListenerOptions} which allows the
 * customization described above but if you create an implementation of this class you might be able
 * to customize it even further
 */
public interface ListenerOptions {

  /**
   * Called before the command is executed. This means before {@link
   * EasyJdaCommand#execute(CommandContext)}
   *
   * @param event the event that is causing a command to be executed
   * @param commandName the alias of the {@link EasyJdaCommand} that is going to be executed
   * @param strings the arguments used to execute the command
   */
  void preCommand(
      @NonNull MessageReceivedEvent event, @NonNull String commandName, @NonNull String[] strings);

  /**
   * Process the result from the command execution, this means providing the {@link
   * me.googas.commands.jda.listener.CommandListener} with a message to send in the {@link
   * net.dv8tion.jda.api.entities.TextChannel} where the command was executed this can be used to
   * create embeds if desired like the implementation {@link DefaultListenerOptions}
   *
   * @param result the result of the command execution which might be null
   * @param context the context of the command execution
   * @return the message to send in the {@link net.dv8tion.jda.api.entities.TextChannel} where the
   *     command was executed. If this message happens to be null no message will be send
   */
  Message processResult(Result result, @NonNull CommandContext context);

  /**
   * Process the consumer of the message from {@link #processResult(Result, CommandContext)}, this
   * means providing the {@link me.googas.commands.jda.listener.CommandListener} with a result for
   * the message sent in the {@link net.dv8tion.jda.api.entities.TextChannel} this can be used to
   * delete the message after a few seconds if desired like the implementation {@link
   * DefaultListenerOptions}
   *
   * @param result the result of the command execution which might be null
   * @param context the context of the command execution
   * @return the result to use in the message to sent in the {@link
   *     net.dv8tion.jda.api.entities.TextChannel} where the command was executed. If this {@link
   *     Consumer} happens to be null nothing will be done using the message
   */
  Consumer<Message> processConsumer(Result result, @NonNull CommandContext context);

  /**
   * Handles any kind of error that is thrown in {@link
   * me.googas.commands.jda.listener.CommandListener}
   *
   * @param fail any kind of exception thrown in {@link
   *     me.googas.commands.jda.listener.CommandListener}
   * @param context the context of the command execution
   */
  void handle(@NonNull Throwable fail, @NonNull CommandContext context);
}

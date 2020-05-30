package com.starfishst.commands.listener;

import com.starfishst.commands.AnnotatedCommand;
import com.starfishst.commands.CommandManager;
import com.starfishst.commands.ManagerOptions;
import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.context.GuildCommandContext;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.commands.result.Result;
import com.starfishst.commands.result.ResultType;
import com.starfishst.commands.utils.embeds.EmbedFactory;
import com.starfishst.commands.utils.message.MessagesFactory;
import com.starfishst.core.utils.Lots;
import com.starfishst.core.utils.Strings;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** The main listener of command execution */
public class CommandListener implements EventListener {

  /** The prefix to start commands */
  @NotNull private final String prefix;
  /** The manager of commands */
  @NotNull private final CommandManager manager;
  /** The options of the manager */
  @NotNull private final ManagerOptions managerOptions;
  /** The provider of messages */
  @NotNull private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param prefix the prefix to listen
   * @param manager the command manager
   * @param managerOptions the options of the manager
   * @param messagesProvider the provider of messages
   */
  public CommandListener(
      @NotNull String prefix,
      @NotNull CommandManager manager,
      @NotNull ManagerOptions managerOptions,
      @NotNull MessagesProvider messagesProvider) {
    this.prefix = prefix;
    this.manager = manager;
    this.managerOptions = managerOptions;
    this.messagesProvider = messagesProvider;
  }

  @Override
  public void onEvent(@NotNull final GenericEvent genericEvent) {
    if (genericEvent instanceof MessageReceivedEvent) {
      this.onMessageReceivedEvent((MessageReceivedEvent) genericEvent);
    }
  }

  /**
   * On the event of a message received
   *
   * @param event the event of a message received
   */
  @SubscribeEvent
  public void onMessageReceivedEvent(@NotNull MessageReceivedEvent event) {
    String[] strings = event.getMessage().getContentRaw().split(" +");
    String commandName = strings[0];
    if (!commandName.startsWith(this.prefix)) return;
    if (managerOptions.isDeleteCommands() && event.getChannelType() != ChannelType.PRIVATE) {
      event.getMessage().delete().queue();
    }
    commandName = commandName.substring(prefix.length());
    AnnotatedCommand command = manager.getCommand(commandName);
    Result result = getResult(event, strings, command, commandName);
    Message response = getMessage(result);
    Consumer<Message> consumer = getConsumer(result);
    if (response != null) {
      if (consumer != null) {
        event.getChannel().sendMessage(response).queue(consumer);
      } else {
        event.getChannel().sendMessage(response).queue();
      }
    }
  }

  /**
   * Get the action to do with the message sent from a result
   *
   * @param result the result to get the action from
   * @return the action from the result or null if it doesn't have any
   */
  @Nullable
  private Consumer<Message> getConsumer(@NotNull Result result) {
    if (result.getSuccess() != null) {
      return result.getSuccess();
    } else if (managerOptions.isDeleteErrors() && result.getType().isError()) {
      return managerOptions.getErrorDeleteConsumer();
    } else if (managerOptions.isDeleteSuccess() && !result.getType().isError()) {
      return managerOptions.getSuccessDeleteConsumer();
    } else {
      return null;
    }
  }

  /**
   * Get the message that will be send from a result
   *
   * @param result the result to get the message from
   * @return the message
   */
  @Nullable
  private Message getMessage(@NotNull Result result) {
    if (result.getDiscordMessage() == null) {
      if (managerOptions.isEmbedMessages()) {
        if (result.getMessage() != null) {
          return EmbedFactory.fromResult(result, this).getAsMessageQuery().getMessage();
        } else {
          return null;
        }
      } else {
        if (result.getMessage() != null) {
          return MessagesFactory.fromString(
                  Strings.buildMessage(
                      messagesProvider.response(
                          result.getType().getTitle(messagesProvider), result.getMessage())))
              .getMessage();
        } else {
          return null;
        }
      }
    } else {
      return result.getDiscordMessage();
    }
  }

  /**
   * Get the result of a command execution
   *
   * @param event the event executing a command
   * @param strings the strings executing the command
   * @param command the command
   * @param commandName the name of the command
   * @return the result of the command execution
   */
  @NotNull
  private Result getResult(
      @NotNull MessageReceivedEvent event,
      @NotNull String[] strings,
      @Nullable AnnotatedCommand command,
      @NotNull String commandName) {
    if (command != null) {
      return command.execute(getCommandContext(event, strings));
    } else {
      return new Result(ResultType.ERROR, messagesProvider.commandNotFound(commandName));
    }
  }

  /**
   * Get the context where the command was executed
   *
   * @param event the event where the command was executed from
   * @param strings the strings representing the arguments of the command
   * @return the context of the command
   */
  @NotNull
  private CommandContext getCommandContext(
      @NotNull MessageReceivedEvent event, @NotNull String[] strings) {
    strings = Lots.arrayFrom(1, strings);
    if (event.getMember() != null) {
      return new GuildCommandContext(
          event.getMessage(),
          event.getAuthor(),
          strings,
          event.getChannel(),
          event,
          messagesProvider);
    } else {
      return new CommandContext(
          event.getMessage(),
          event.getAuthor(),
          strings,
          event.getChannel(),
          event,
          messagesProvider);
    }
  }

  /**
   * Get the manager executing the commands
   *
   * @return the command manager
   */
  @NotNull
  public CommandManager getManager() {
    return manager;
  }

  /**
   * Get the messages provider of the listener
   *
   * @return the messages provider
   */
  @NotNull
  public MessagesProvider getMessagesProvider() {
    return messagesProvider;
  }

  /**
   * Get the options of the manager
   *
   * @return the options
   */
  @NotNull
  public ManagerOptions getManagerOptions() {
    return managerOptions;
  }
}

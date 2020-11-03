package com.starfishst.jda.listener;

import com.starfishst.jda.AnnotatedCommand;
import com.starfishst.jda.CommandManager;
import com.starfishst.jda.ManagerOptions;
import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.context.GuildCommandContext;
import com.starfishst.jda.messages.MessagesProvider;
import com.starfishst.jda.result.Result;
import com.starfishst.jda.result.ResultType;
import com.starfishst.jda.utils.embeds.EmbedFactory;
import com.starfishst.jda.utils.message.MessagesFactory;
import java.util.function.Consumer;
import me.googas.commons.Lots;
import me.googas.commons.Strings;
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
    if (!commandName.startsWith(this.prefix)) {
      return;
    }
    if (managerOptions.isDeleteCommands() && event.getChannelType() != ChannelType.PRIVATE) {
      event.getMessage().delete().queue();
    }
    commandName = commandName.substring(prefix.length());
    AnnotatedCommand command = manager.getCommand(commandName);
    CommandContext context =
        getCommandContext(event, strings, command == null ? null : command.getName());
    Result result = getResult(command, commandName, context);
    Message response = getMessage(result, context);
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
  public Consumer<Message> getConsumer(@NotNull Result result) {
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
   * @param context the context of the command
   * @return the message
   */
  @Nullable
  private Message getMessage(@Nullable Result result, CommandContext context) {
    if (result != null && result.getDiscordMessage() == null) {
      if (managerOptions.isEmbedMessages()) {
        if (result.getMessage() != null) {
          return EmbedFactory.fromResult(result, this, context).getAsMessageQuery().getMessage();
        } else {
          return null;
        }
      } else {
        if (result.getMessage() != null) {
          return MessagesFactory.fromString(
                  Strings.buildMessage(
                      messagesProvider.response(
                          result.getType().getTitle(messagesProvider, context),
                          result.getMessage(),
                          context)))
              .getMessage();
        } else {
          return null;
        }
      }
    } else if (result != null) {
      return result.getDiscordMessage();
    }
    return null;
  }

  /**
   * Get the result of a command execution
   *
   * @param command the command
   * @param commandName the name of the command
   * @param context the context of the command
   * @return the result of the command execution
   */
  @Nullable
  private Result getResult(
      @Nullable AnnotatedCommand command, @NotNull String commandName, CommandContext context) {
    if (command != null) {
      return command.execute(context);
    } else {
      return new Result(ResultType.ERROR, messagesProvider.commandNotFound(commandName, context));
    }
  }

  /**
   * Get the context where the command was executed
   *
   * @param event the event where the command was executed from
   * @param strings the strings representing the arguments of the command
   * @param commandName the name of the command
   * @return the context of the command
   */
  @NotNull
  private CommandContext getCommandContext(
      @NotNull MessageReceivedEvent event,
      @NotNull String[] strings,
      @Nullable String commandName) {
    strings = Lots.arrayFrom(1, strings);
    if (event.getMember() != null) {
      return new GuildCommandContext(
          event.getMessage(),
          event.getAuthor(),
          strings,
          event.getChannel(),
          event,
          messagesProvider,
          manager.getRegistry(),
          commandName);
    } else {
      return new CommandContext(
          event.getMessage(),
          event.getAuthor(),
          strings,
          event.getChannel(),
          messagesProvider,
          manager.getRegistry(),
          commandName);
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

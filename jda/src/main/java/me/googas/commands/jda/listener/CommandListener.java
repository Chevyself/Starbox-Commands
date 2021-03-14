package me.googas.commands.jda.listener;

import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.annotations.Nullable;
import me.googas.commands.jda.AnnotatedCommand;
import me.googas.commands.jda.CommandManager;
import me.googas.commands.jda.ManagerOptions;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.context.GuildCommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.result.Result;
import me.googas.commands.jda.result.ResultType;
import me.googas.commands.jda.utils.embeds.EmbedFactory;
import me.googas.commands.jda.utils.message.FakeMessage;
import me.googas.commands.jda.utils.message.MessagesFactory;
import me.googas.starbox.Lots;
import me.googas.starbox.Strings;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

/** The main listener of command execution */
public class CommandListener implements EventListener {

  @NonNull @Getter private final CommandManager manager;
  @NonNull @Getter private final ManagerOptions managerOptions;
  @NonNull @Getter private final MessagesProvider messagesProvider;
  @NonNull @Getter @Setter private String prefix;

  /**
   * Create an instance
   *
   * @param prefix the prefix to listen
   * @param manager the command manager
   * @param managerOptions the options of the manager
   * @param messagesProvider the provider of messages
   */
  public CommandListener(
      @NonNull String prefix,
      @NonNull CommandManager manager,
      @NonNull ManagerOptions managerOptions,
      @NonNull MessagesProvider messagesProvider) {
    this.prefix = prefix;
    this.manager = manager;
    this.managerOptions = managerOptions;
    this.messagesProvider = messagesProvider;
  }

  public void dispatch(@NonNull User user, @Nullable Member member, Message message) {
    this.onMessageReceivedEvent(
        new MessageReceivedEvent(this.manager.getJda(), 0, new FakeMessage(user, member, message)));
  }

  /**
   * On the event of a message received
   *
   * @param event the event of a message received
   */
  @SubscribeEvent
  public void onMessageReceivedEvent(@NonNull MessageReceivedEvent event) {
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
      if (consumer != null && !(event.getMessage() instanceof FakeMessage)) {
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
  public Consumer<Message> getConsumer(Result result) {
    if (result != null) {
      if (result.getSuccess() != null) {
        return result.getSuccess();
      } else if (managerOptions.isDeleteErrors() && result.getType().isError()) {
        return managerOptions.getErrorDeleteConsumer();
      } else if (managerOptions.isDeleteSuccess() && !result.getType().isError()) {
        return managerOptions.getSuccessDeleteConsumer();
      }
    }
    return null;
  }

  /**
   * Get the message that will be send from a result
   *
   * @param result the result to get the message from
   * @param context the context of the command
   * @return the message
   */
  private Message getMessage(Result result, CommandContext context) {
    if (result != null && result.getDiscordMessage() == null) {
      if (managerOptions.isEmbedMessages()) {
        if (result.getMessage() != null) {
          return EmbedFactory.fromResult(result, this, context).getAsMessageQuery().build();
        } else {
          return null;
        }
      } else {
        if (result.getMessage() != null) {
          return MessagesFactory.fromString(
                  Strings.build(
                      messagesProvider.response(
                          result.getType().getTitle(messagesProvider, context),
                          result.getMessage(),
                          context)))
              .build();
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
  private Result getResult(
      AnnotatedCommand command, @NonNull String commandName, CommandContext context) {
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
  @NonNull
  private CommandContext getCommandContext(
      @NonNull MessageReceivedEvent event, @NonNull String[] strings, String commandName) {
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

  @Override
  public void onEvent(@NonNull @NotNull final GenericEvent genericEvent) {
    if (genericEvent instanceof MessageReceivedEvent) {
      this.onMessageReceivedEvent((MessageReceivedEvent) genericEvent);
    }
  }
}

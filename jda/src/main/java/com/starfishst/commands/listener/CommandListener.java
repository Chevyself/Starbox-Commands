package com.starfishst.commands.listener;

import com.starfishst.commands.AnnotatedCommand;
import com.starfishst.commands.CommandManager;
import com.starfishst.commands.ManagerOptions;
import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.context.GuildCommandContext;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.commands.result.Result;
import com.starfishst.commands.result.ResultType;
import com.starfishst.commands.utils.MessagesFactory;
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

public class CommandListener implements EventListener {

  @NotNull
  private final String prefix;
  @NotNull
  private final CommandManager manager;
  @NotNull
  private final ManagerOptions managerOptions;
  @NotNull
  private final MessagesProvider messagesProvider;

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

  @SubscribeEvent
  public void onMessageReceivedEvent(@NotNull MessageReceivedEvent event) {
    String[] strings = event.getMessage().getContentRaw().split(" +");
    String commandName = strings[0];
    if (!commandName.startsWith(this.prefix)) return;
    if (managerOptions.isDeleteCommands() && event.getChannelType() != ChannelType.PRIVATE) {
      event.getMessage().delete().queue();
    }
    AnnotatedCommand command = manager.getCommand(commandName.substring(prefix.length()));
    Result result = getResult(event, strings, command);
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

  @Nullable
  private Consumer<Message> getConsumer(@NotNull Result result) {
    if (result.getSuccess() != null) {
      return result.getSuccess();
    } else if (managerOptions.isDeleteErrors() && result.getType().isError()) {
      return message ->
              message
                      .delete()
                      .queueAfter(managerOptions.getDeleteTime(), managerOptions.getDeleteUnit());
    } else {
      return null;
    }
  }

  @Nullable
  private Message getMessage(@NotNull Result result) {
    if (result.getDiscordMessage() == null) {
      if (managerOptions.isEmbedMessages()) {
        if (result.getMessage() != null) {
          return MessagesFactory.fromResult(result, this);
        } else {
          return null;
        }
      } else {
        if (result.getMessage() != null) {
          return MessagesFactory.fromString(
                  Strings.buildMessage(
                          messagesProvider.response(),
                          result.getType().getTitle(messagesProvider),
                          result.getMessage()));
        } else {
          return null;
        }
      }
    } else {
      return result.getDiscordMessage();
    }
  }

  @NotNull
  private Result getResult(
          @NotNull MessageReceivedEvent event,
          @NotNull String[] strings,
          @Nullable AnnotatedCommand command) {
    if (command != null) {
      return (Result) command.execute(getCommandContext(event, strings));
    } else {
      return new Result(ResultType.ERROR, messagesProvider.commandNotFound());
    }
  }

  @NotNull
  private CommandContext getCommandContext(
          @NotNull MessageReceivedEvent event, @NotNull String[] strings) {
    strings = Lots.arrayFrom(1, strings);
    if (event.getMember() != null) {
      return new GuildCommandContext(
              event.getMessage(), event.getAuthor(), strings, event.getChannel(), event);
    } else {
      return new CommandContext(
              event.getMessage(), event.getAuthor(), strings, event.getChannel(), event);
    }
  }

  @NotNull
  public CommandManager getManager() {
    return manager;
  }

  @NotNull
  public MessagesProvider getMessagesProvider() {
    return messagesProvider;
  }

  @NotNull
  public ManagerOptions getManagerOptions() {
    return managerOptions;
  }
}

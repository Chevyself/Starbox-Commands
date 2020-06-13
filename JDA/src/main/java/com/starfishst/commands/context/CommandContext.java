package com.starfishst.commands.context;

import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.commands.providers.registry.ImplProvidersRegistry;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import com.starfishst.core.utils.Strings;
import java.util.Arrays;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This context is used for every command {@link User being the sender} */
public class CommandContext implements ICommandContext {

  /** The message that executed the command */
  @NotNull private final Message message;
  /** The user that executed the command */
  @NotNull private final User sender;
  /** The channel where the command was invoked */
  @NotNull private final MessageChannel channel;
  /**
   * The event where the command started. Sometimes it could be that it was not executed from an
   * actual event
   */
  @Nullable private final MessageReceivedEvent event;
  /** The strings of the command execution */
  @NotNull private String[] strings;
  /** The messages provider of this context */
  @NotNull private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param message the message where the command was executed
   * @param sender the sender of the command
   * @param args the strings send in the command
   * @param channel the channel where the command was executed
   * @param event the event where the command was executed
   * @param messagesProvider the messages provider for this context
   */
  public CommandContext(
      @NotNull Message message,
      @NotNull User sender,
      @NotNull String[] args,
      @NotNull MessageChannel channel,
      @Nullable MessageReceivedEvent event,
      @NotNull MessagesProvider messagesProvider) {
    this.message = message;
    this.sender = sender;
    this.strings = args;
    this.channel = channel;
    this.event = event;
    this.messagesProvider = messagesProvider;
  }

  /**
   * This method is mostly used when the command is a child to remove the child alias.
   *
   * @param strings the new strings
   */
  public void setStrings(@NotNull String[] strings) {
    this.strings = strings;
  }

  /**
   * Get the message that executed the command
   *
   * @return the message
   */
  @NotNull
  public Message getMessage() {
    return message;
  }

  /**
   * Get the channel where the command was executed
   *
   * @return the channel
   */
  @NotNull
  public MessageChannel getChannel() {
    return channel;
  }

  /**
   * Get the event where the command was executed
   *
   * @return the event
   */
  @Nullable
  public MessageReceivedEvent getEvent() {
    return event;
  }

  @NotNull
  @Override
  public User getSender() {
    return sender;
  }

  @NotNull
  @Override
  public String getString() {
    return Strings.fromArray(strings);
  }

  @NotNull
  @Override
  public String[] getStrings() {
    return strings;
  }

  @Override
  public ProvidersRegistry<CommandContext> getRegistry() {
    return ImplProvidersRegistry.getInstance();
  }

  @NotNull
  @Override
  public MessagesProvider getMessagesProvider() {
    return messagesProvider;
  }

  @Override
  public boolean hasFlag(@NotNull String flag) {
    for (String string : this.strings) {
      if (string.equalsIgnoreCase(flag)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return "CommandContext{"
        + "message="
        + message
        + ", sender="
        + sender
        + ", strings="
        + Arrays.toString(strings)
        + ", channel="
        + channel
        + ", event="
        + event
        + '}';
  }
}

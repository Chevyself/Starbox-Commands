package com.starfishst.commands.jda.context;

import com.starfishst.commands.jda.messages.MessagesProvider;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.providers.registry.ProvidersRegistry;
import java.util.Arrays;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Delegate;
import me.googas.commons.Strings;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

/** This context is used for every command {@link User being the sender} */
public class CommandContext implements ICommandContext {

  /** The message that executed the command */
  @NonNull @Getter private final Message message;

  @NonNull private final User sender;
  @NonNull @Getter private final MessageChannel channel;
  @NonNull private final MessagesProvider messagesProvider;
  @NonNull @Delegate private final ProvidersRegistry<CommandContext> registry;
  @Getter private final String commandName;
  @NonNull @Setter private String[] strings;

  /**
   * Create an instance
   *
   * @param message the message where the command was executed
   * @param sender the sender of the command
   * @param args the strings send in the command
   * @param channel the channel where the command was executed
   * @param messagesProvider the messages provider for this context
   * @param registry the registry of the command context
   * @param commandName the name of the command that is being executed
   */
  public CommandContext(
      @NonNull Message message,
      @NonNull User sender,
      @NonNull String[] args,
      @NonNull MessageChannel channel,
      @NonNull MessagesProvider messagesProvider,
      @NonNull ProvidersRegistry<CommandContext> registry,
      String commandName) {
    this.message = message;
    this.sender = sender;
    this.strings = args;
    this.channel = channel;
    this.messagesProvider = messagesProvider;
    this.registry = registry;
    this.commandName = commandName;
  }

  @NonNull
  @Override
  public User getSender() {
    return sender;
  }

  @NonNull
  @Override
  public String getString() {
    return Strings.fromArray(strings);
  }

  @NonNull
  @Override
  public String[] getStrings() {
    return strings;
  }

  @NonNull
  @Override
  public ProvidersRegistry<CommandContext> getRegistry() {
    return registry;
  }

  @NonNull
  @Override
  public MessagesProvider getMessagesProvider() {
    return messagesProvider;
  }

  @Override
  public boolean hasFlag(@NonNull String flag) {
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
        + '}';
  }
}

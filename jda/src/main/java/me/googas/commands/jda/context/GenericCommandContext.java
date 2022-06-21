package me.googas.commands.jda.context;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.commands.jda.CommandManager;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.commands.util.Strings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/** This context is used for every command {@link User being the sender}. */
public class GenericCommandContext implements CommandContext {

  @NonNull @Getter protected final CommandManager manager;
  @NonNull @Getter protected final JDA jda;
  @NonNull @Getter protected final MessageReceivedEvent event;
  @NonNull protected final User sender;
  @NonNull protected final MessageChannel channel;
  @NonNull protected final MessagesProvider messagesProvider;
  @NonNull protected final ProvidersRegistry<CommandContext> registry;
  @Getter protected final String commandName;
  protected final Message message;
  @NonNull @Setter protected String[] strings;

  /**
   * Create an instance.
   *
   * @param manager
   * @param jda the jda instance in which the {@link CommandManager} is registered
   * @param event the event of the message that executes the command
   * @param sender the sender of the command
   * @param args the strings send in the command
   * @param channel the channel where the command was executed
   * @param messagesProvider the messages provider for this context
   * @param registry the registry of the command context
   * @param commandName the name of the command that is being executed
   * @param message the message where the command was executed
   */
  public GenericCommandContext(
      @NonNull CommandManager manager,
      @NonNull JDA jda,
      @NonNull MessageReceivedEvent event,
      @NonNull User sender,
      @NonNull String[] args,
      @NonNull MessageChannel channel,
      @NonNull MessagesProvider messagesProvider,
      @NonNull ProvidersRegistry<CommandContext> registry,
      String commandName,
      Message message) {
    this.manager = manager;
    this.jda = jda;
    this.event = event;
    this.sender = sender;
    this.strings = args;
    this.channel = channel;
    this.messagesProvider = messagesProvider;
    this.registry = registry;
    this.commandName = commandName;
    this.message = message;
  }

  @Override
  public @NonNull Optional<MessageChannel> getChannel() {
    return Optional.of(channel);
  }

  @NonNull
  public Optional<Message> getMessage() {
    return Optional.ofNullable(message);
  }

  @NonNull
  @Override
  public User getSender() {
    return this.sender;
  }

  @NonNull
  @Override
  public String getString() {
    return Strings.join(this.strings);
  }

  @NonNull
  @Override
  public String[] getStrings() {
    return this.strings;
  }

  @NonNull
  @Override
  public ProvidersRegistry<CommandContext> getRegistry() {
    return this.registry;
  }

  @NonNull
  @Override
  public MessagesProvider getMessagesProvider() {
    return this.messagesProvider;
  }

  @Override
  public @NonNull GenericCommandContext getChildren() {
    return new GenericCommandContext(
        manager,
        this.jda,
        event,
        this.sender,
        Arrays.copyOfRange(strings, 1, strings.length),
        this.channel,
        this.messagesProvider,
        this.registry,
        this.commandName,
        this.message);
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
        + this.message
        + ", sender="
        + this.sender
        + ", strings="
        + Arrays.toString(this.strings)
        + ", channel="
        + this.channel
        + '}';
  }
}

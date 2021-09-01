package me.googas.commands.jda.context;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.providers.registry.ProvidersRegistry;
import me.googas.starbox.Strings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

/** This context is used for every command {@link User being the sender}. */
public class GenericCommandContext implements CommandContext {

  @NonNull @Getter private final JDA jda;
  @NonNull private final User sender;
  @NonNull private final MessageChannel channel;
  @NonNull private final MessagesProvider messagesProvider;
  @NonNull private final ProvidersRegistry<CommandContext> registry;
  @Getter private final String commandName;
  /** The message that executed the command. */
  private final Message message;
  @NonNull @Setter private String[] strings;

  /**
   * Create an instance.
   *
   * @param jda the jda instance in which the {@link me.googas.commands.jda.CommandManager} is
   *     registered
   * @param sender the sender of the command
   * @param args the strings send in the command
   * @param channel the channel where the command was executed
   * @param messagesProvider the messages provider for this context
   * @param registry the registry of the command context
   * @param commandName the name of the command that is being executed
   * @param message the message where the command was executed
   */
  public GenericCommandContext(
      @NonNull JDA jda,
      @NonNull User sender,
      @NonNull String[] args,
      @NonNull MessageChannel channel,
      @NonNull MessagesProvider messagesProvider,
      @NonNull ProvidersRegistry<CommandContext> registry,
      String commandName,
      Message message) {
    this.jda = jda;
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
    return Strings.fromArray(this.strings);
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

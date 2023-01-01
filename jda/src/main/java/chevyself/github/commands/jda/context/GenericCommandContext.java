package chevyself.github.commands.jda.context;

import chevyself.github.commands.flags.FlagArgument;
import chevyself.github.commands.jda.CommandManager;
import chevyself.github.commands.jda.JdaCommand;
import chevyself.github.commands.jda.messages.MessagesProvider;
import chevyself.github.commands.providers.registry.ProvidersRegistry;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/** This context is used for every command {@link User being the sender}. */
public class GenericCommandContext implements CommandContext {

  @NonNull @Getter protected final JDA jda;
  @NonNull @Getter protected final JdaCommand command;
  @NonNull @Getter protected final User sender;
  @NonNull @Getter protected final String string;
  @NonNull @Getter protected final String[] strings;
  @NonNull @Getter protected final ProvidersRegistry<CommandContext> registry;
  @NonNull @Getter protected final MessagesProvider messagesProvider;
  @NonNull @Getter protected final List<FlagArgument> flags;
  @NonNull @Getter protected final MessageReceivedEvent event;
  @NonNull protected final MessageChannel channel;
  protected final Message message;

  /**
   * Create an instance.
   *
   * @param jda the jda instance in which the {@link CommandManager} is registered
   * @param command the command for which this context was created
   * @param sender the sender of the command
   * @param string the input strings joined
   * @param args the strings send in the command
   * @param registry the registry of the command context
   * @param messagesProvider the messages' provider for this context
   * @param flags the flags in the input of the command
   * @param event the event of the message that executes the command
   * @param channel the channel where the command was executed
   * @param message the message where the command was executed
   */
  public GenericCommandContext(
      @NonNull JDA jda,
      @NonNull JdaCommand command,
      @NonNull User sender,
      @NonNull String string,
      @NonNull String[] args,
      @NonNull ProvidersRegistry<CommandContext> registry,
      @NonNull MessagesProvider messagesProvider,
      @NonNull List<FlagArgument> flags,
      @NonNull MessageReceivedEvent event,
      @NonNull MessageChannel channel,
      Message message) {
    this.jda = jda;
    this.command = command;
    this.sender = sender;
    this.string = string;
    this.strings = args;
    this.registry = registry;
    this.messagesProvider = messagesProvider;
    this.flags = flags;
    this.event = event;
    this.channel = channel;
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

  @Override
  public @NonNull GenericCommandContext getChildren() {
    return new GenericCommandContext(
        this.jda,
        command,
        this.sender,
        string,
        Arrays.copyOfRange(strings, 1, strings.length),
        this.registry,
        this.messagesProvider,
        flags,
        event,
        this.channel,
        this.message);
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

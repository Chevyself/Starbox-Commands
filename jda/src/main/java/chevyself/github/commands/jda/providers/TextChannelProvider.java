package chevyself.github.commands.jda.providers;

import chevyself.github.commands.StarboxCommandManager;
import chevyself.github.commands.exceptions.ArgumentProviderException;
import chevyself.github.commands.jda.context.CommandContext;
import chevyself.github.commands.jda.messages.MessagesProvider;
import chevyself.github.commands.jda.providers.type.JdaArgumentProvider;
import chevyself.github.commands.jda.providers.type.JdaExtraArgumentProvider;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

/** Provides the {@link StarboxCommandManager} with a {@link User}. */
public class TextChannelProvider
    implements JdaArgumentProvider<TextChannel>, JdaExtraArgumentProvider<TextChannel> {

  private final MessagesProvider messagesProvider;

  /**
   * Create an instance.
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public TextChannelProvider(MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NonNull
  @Override
  public TextChannel fromString(@NonNull String string, @NonNull CommandContext context)
      throws ArgumentProviderException {
    TextChannel channel =
        context.getJda().getTextChannelById(UserProvider.getIdFromMention(string));
    if (channel != null) {
      return channel;
    }
    throw new ArgumentProviderException(this.messagesProvider.invalidTextChannel(string, context));
  }

  @Override
  public @NonNull Class<TextChannel> getClazz() {
    return TextChannel.class;
  }

  @Override
  public @NonNull TextChannel getObject(@NonNull CommandContext context)
      throws ArgumentProviderException {
    if (context.getChannel().isPresent()) {
      MessageChannel messageChannel = context.getChannel().get();
      if (messageChannel instanceof TextChannel) {
        return (TextChannel) messageChannel;
      }
      // The only other option is that the channel was a private channel
      throw new ArgumentProviderException(this.messagesProvider.guildOnly(context));
    } else {
      throw new ArgumentProviderException(this.messagesProvider.noMessage(context));
    }
  }
}

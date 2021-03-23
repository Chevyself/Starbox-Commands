package me.googas.commands.jda.providers;

import lombok.NonNull;
import me.googas.commands.EasyCommandManager;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.providers.type.JdaArgumentProvider;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

/** Provides the {@link EasyCommandManager} with a {@link User} */
public class TextChannelProvider implements JdaArgumentProvider<TextChannel> {

  private final MessagesProvider messagesProvider;

  /**
   * Create an instance
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
    for (TextChannel channel : context.getMessage().getMentionedChannels()) {
      if (string.contains(channel.getId())) {
        return channel;
      }
    }
    throw new ArgumentProviderException(messagesProvider.invalidTextChannel(string, context));
  }

  @Override
  public @NonNull Class<TextChannel> getClazz() {
    return TextChannel.class;
  }
}

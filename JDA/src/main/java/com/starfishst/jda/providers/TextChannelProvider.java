package com.starfishst.jda.providers;

import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.messages.MessagesProvider;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link User} */
public class TextChannelProvider implements IArgumentProvider<TextChannel, CommandContext> {

  /** The provider to give the error message */
  private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public TextChannelProvider(MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NotNull
  @Override
  public TextChannel fromString(@NotNull String string, @NotNull CommandContext context)
      throws ArgumentProviderException {
    for (TextChannel channel : context.getMessage().getMentionedChannels()) {
      if (string.contains(channel.getId())) {
        return channel;
      }
    }
    throw new ArgumentProviderException(messagesProvider.invalidTextChannel(string, context));
  }

  @Override
  public @NotNull Class<TextChannel> getClazz() {
    return TextChannel.class;
  }
}

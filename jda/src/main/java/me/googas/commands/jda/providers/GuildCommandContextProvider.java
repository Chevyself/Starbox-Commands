package me.googas.commands.jda.providers;

import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.context.GuildCommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.providers.type.JdaExtraArgumentProvider;
import me.googas.commands.ICommandManager;
import me.googas.commands.exceptions.ArgumentProviderException;
import lombok.NonNull;

/** Provides the {@link ICommandManager} with a {@link GuildCommandContext} */
public class GuildCommandContextProvider implements JdaExtraArgumentProvider<GuildCommandContext> {

  private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public GuildCommandContextProvider(MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NonNull
  @Override
  public GuildCommandContext getObject(@NonNull CommandContext context)
      throws ArgumentProviderException {
    if (!(context instanceof GuildCommandContext)) {
      throw new ArgumentProviderException(messagesProvider.guildOnly(context));
    }
    return (GuildCommandContext) context;
  }

  @Override
  public @NonNull Class<GuildCommandContext> getClazz() {
    return GuildCommandContext.class;
  }
}

package chevyself.github.commands.jda.providers;

import chevyself.github.commands.StarboxCommandManager;
import chevyself.github.commands.exceptions.ArgumentProviderException;
import chevyself.github.commands.jda.context.CommandContext;
import chevyself.github.commands.jda.context.GuildCommandContext;
import chevyself.github.commands.jda.messages.MessagesProvider;
import chevyself.github.commands.jda.providers.type.JdaExtraArgumentProvider;
import lombok.NonNull;

/** Provides the {@link StarboxCommandManager} with a {@link GuildCommandContext}. */
public class GuildCommandContextProvider implements JdaExtraArgumentProvider<GuildCommandContext> {

  private final MessagesProvider messagesProvider;

  /**
   * Create an instance.
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
      throw new ArgumentProviderException(this.messagesProvider.guildOnly(context));
    }
    return (GuildCommandContext) context;
  }

  @Override
  public @NonNull Class<GuildCommandContext> getClazz() {
    return GuildCommandContext.class;
  }
}

package com.starfishst.commands.jda.providers;

import com.starfishst.commands.jda.context.CommandContext;
import com.starfishst.commands.jda.context.GuildCommandContext;
import com.starfishst.commands.jda.messages.MessagesProvider;
import com.starfishst.commands.jda.providers.type.JdaExtraArgumentProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Guild;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link Guild} */
public class GuildProvider implements JdaExtraArgumentProvider<Guild> {

  private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public GuildProvider(MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NonNull
  @Override
  public Guild getObject(@NonNull CommandContext context) throws ArgumentProviderException {
    if (!(context instanceof GuildCommandContext)) {
      throw new ArgumentProviderException(messagesProvider.guildOnly(context));
    }
    return ((GuildCommandContext) context).getGuild();
  }

  @Override
  public @NonNull Class<Guild> getClazz() {
    return Guild.class;
  }
}

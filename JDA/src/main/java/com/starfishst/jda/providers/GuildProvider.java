package com.starfishst.jda.providers;

import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.context.GuildCommandContext;
import com.starfishst.jda.messages.MessagesProvider;
import com.starfishst.jda.providers.type.JdaExtraArgumentProvider;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link Guild} */
public class GuildProvider implements JdaExtraArgumentProvider<Guild> {

  /** The provider to give the error message */
  private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public GuildProvider(MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NotNull
  @Override
  public Guild getObject(@NotNull CommandContext context) throws ArgumentProviderException {
    if (!(context instanceof GuildCommandContext)) {
      throw new ArgumentProviderException(messagesProvider.guildOnly(context));
    }
    return ((GuildCommandContext) context).getGuild();
  }

  @Override
  public @NotNull Class<Guild> getClazz() {
    return Guild.class;
  }
}

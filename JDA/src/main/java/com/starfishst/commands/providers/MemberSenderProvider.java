package com.starfishst.commands.providers;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.context.GuildCommandContext;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link User} */
public class MemberSenderProvider implements IExtraArgumentProvider<Member, CommandContext> {

  /** The provider to give the error message */
  private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public MemberSenderProvider(MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NotNull
  @Override
  public Member getObject(@NotNull CommandContext context) throws ArgumentProviderException {
    if (!(context instanceof GuildCommandContext)) {
      throw new ArgumentProviderException(messagesProvider.guildOnly());
    } else {
      return ((GuildCommandContext) context).getMember();
    }
  }

  @Override
  public @NotNull Class<Member> getClazz() {
    return Member.class;
  }
}

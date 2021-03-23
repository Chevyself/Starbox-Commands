package me.googas.commands.jda.providers;

import lombok.NonNull;
import me.googas.commands.EasyCommandManager;
import me.googas.commands.exceptions.ArgumentProviderException;
import me.googas.commands.jda.context.CommandContext;
import me.googas.commands.jda.context.GuildCommandContext;
import me.googas.commands.jda.messages.MessagesProvider;
import me.googas.commands.jda.providers.type.JdaExtraArgumentProvider;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

/** Provides the {@link EasyCommandManager} with a {@link User} */
public class MemberSenderProvider implements JdaExtraArgumentProvider<Member> {

  private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public MemberSenderProvider(MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NonNull
  @Override
  public Member getObject(@NonNull CommandContext context) throws ArgumentProviderException {
    if (!(context instanceof GuildCommandContext)) {
      throw new ArgumentProviderException(messagesProvider.guildOnly(context));
    } else {
      return ((GuildCommandContext) context).getMember();
    }
  }

  @Override
  public @NonNull Class<Member> getClazz() {
    return Member.class;
  }
}

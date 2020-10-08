package com.starfishst.jda.providers;

import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.jda.context.CommandContext;
import com.starfishst.jda.context.GuildCommandContext;
import com.starfishst.jda.messages.MessagesProvider;
import com.starfishst.jda.providers.type.JdaArgumentProvider;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link Member} */
public class MemberProvider implements JdaArgumentProvider<Member> {

  /** The provider to give the error message */
  private final MessagesProvider messagesProvider;

  /**
   * Create an instance
   *
   * @param messagesProvider to send the error message in case that the long could not be parsed
   */
  public MemberProvider(MessagesProvider messagesProvider) {
    this.messagesProvider = messagesProvider;
  }

  @NotNull
  @Override
  public Member fromString(@NotNull String string, @NotNull CommandContext context)
      throws ArgumentProviderException {
    if (!(context instanceof GuildCommandContext)) {
      throw new ArgumentProviderException(messagesProvider.guildOnly(context));
    } else {
      for (Member member : context.getMessage().getMentionedMembers()) {
        if (string.contains(member.getId())) {
          return member;
        }
      }
      throw new ArgumentProviderException(messagesProvider.invalidMember(string, context));
    }
  }

  @Override
  public @NotNull Class<Member> getClazz() {
    return Member.class;
  }
}

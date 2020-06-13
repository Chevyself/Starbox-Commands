package com.starfishst.commands.providers;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.commands.context.GuildCommandContext;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

/** Provides the {@link com.starfishst.core.ICommandManager} with a {@link Member} */
public class MemberProvider implements IArgumentProvider<Member, CommandContext> {

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
      Member member =
          context.getMessage().getMentionedMembers().stream()
              .filter(mentionedMember -> string.contains(mentionedMember.getId()))
              .findFirst()
              .orElse(null);
      if (member != null) {
        return member;
      } else {
        throw new ArgumentProviderException(messagesProvider.invalidMember(string, context));
      }
    }
  }

  @Override
  public @NotNull Class<Member> getClazz() {
    return Member.class;
  }
}

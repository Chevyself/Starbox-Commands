package com.starfishst.commands.providers;

import com.starfishst.commands.context.GuildCommandContext;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class MemberProvider implements IArgumentProvider<Member> {

    @NotNull
    @Override
    public Member fromString(@NotNull String string, @NotNull ICommandContext<?> context)
            throws ArgumentProviderException {
        if (context instanceof GuildCommandContext) {
            for (Member member : ((GuildCommandContext) context).getMessage().getMentionedMembers()) {
                if (member.getAsMention().equalsIgnoreCase(string)) {
                    return member;
                }
            }
        } else {
            throw new ArgumentProviderException("This command may only be executed in a guild");
        }
        throw new ArgumentProviderException("{0} is not a valid user", string);
    }

    @Override
    public @NotNull Class<Member> getClazz() {
        return Member.class;
    }
}

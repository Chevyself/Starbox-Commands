package com.starfishst.commands.providers;

import com.starfishst.commands.context.GuildCommandContext;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

public class MemberSenderProvider implements IExtraArgumentProvider<Member> {
    @NotNull
    @Override
    public Member getObject(@NotNull ICommandContext<?> context) throws ArgumentProviderException {
        if (context instanceof GuildCommandContext) {
            return ((GuildCommandContext) context).getMember();
        }
        throw new ArgumentProviderException("This command was meant to be used in a guild");
    }

    @Override
    public @NotNull Class<Member> getClazz() {
        return Member.class;
    }
}

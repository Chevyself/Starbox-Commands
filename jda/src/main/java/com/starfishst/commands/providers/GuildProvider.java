package com.starfishst.commands.providers;

import com.starfishst.commands.context.GuildCommandContext;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

public class GuildProvider implements IExtraArgumentProvider<Guild> {

    @NotNull
    @Override
    public Guild getObject(@NotNull ICommandContext<?> context) throws ArgumentProviderException {
        if (context instanceof GuildCommandContext) {
            return ((GuildCommandContext) context).getGuild();
        }
        throw new ArgumentProviderException("This command may only be used in a guild");
    }

    @Override
    public @NotNull Class<Guild> getClazz() {
        return Guild.class;
    }
}

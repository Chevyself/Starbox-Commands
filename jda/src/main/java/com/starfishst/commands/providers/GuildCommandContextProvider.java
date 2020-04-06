package com.starfishst.commands.providers;

import com.starfishst.commands.context.GuildCommandContext;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import org.jetbrains.annotations.NotNull;

public class GuildCommandContextProvider implements IExtraArgumentProvider<GuildCommandContext> {

    @NotNull
    @Override
    public GuildCommandContext getObject(@NotNull ICommandContext<?> context)
            throws ArgumentProviderException {
        if (context instanceof GuildCommandContext) {
            return (GuildCommandContext) context;
        }
        throw new ArgumentProviderException("This command may only be used in a guild.");
    }

    @Override
    public @NotNull Class<GuildCommandContext> getClazz() {
        return GuildCommandContext.class;
    }
}

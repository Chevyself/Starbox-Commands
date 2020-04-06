package com.starfishst.commands.providers;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IExtraArgumentProvider;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public class MessageProvider implements IExtraArgumentProvider<Message> {

    @Override
    public @NotNull Class<Message> getClazz() {
        return Message.class;
    }

    @NotNull
    @Override
    public Message getObject(@NotNull ICommandContext<?> context) throws ArgumentProviderException {
        if (context instanceof CommandContext) {
            return ((CommandContext) context).getMessage();
        }
        throw new ArgumentProviderException("This command was not executed from a Discord command.");
    }
}

package com.starfishst.commands.providers;

import com.starfishst.commands.context.CommandContext;
import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.exceptions.ArgumentProviderException;
import com.starfishst.core.providers.type.IArgumentProvider;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

public class TextChannelProvider implements IArgumentProvider<TextChannel> {

    @NotNull
    @Override
    public TextChannel fromString(@NotNull String string, @NotNull ICommandContext<?> context)
            throws ArgumentProviderException {
        if (context instanceof CommandContext) {
            for (TextChannel channel : ((CommandContext) context).getMessage().getMentionedChannels()) {
                if (channel.getAsMention().equalsIgnoreCase(string)) {
                    return channel;
                }
            }
        }
        throw new ArgumentProviderException("{0} is not a channel", string);
    }

    @Override
    public @NotNull Class<TextChannel> getClazz() {
        return TextChannel.class;
    }
}

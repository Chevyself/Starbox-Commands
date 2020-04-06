package com.starfishst.commands.utils;

import com.starfishst.commands.listener.CommandListener;
import com.starfishst.commands.messages.MessagesProvider;
import com.starfishst.commands.result.Result;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

public class MessagesFactory {

    private static final MessageBuilder messageBuilder = new MessageBuilder();
    private static final EmbedBuilder embedBuilder = new EmbedBuilder();

    public static @NotNull Message fromString(@NotNull final String s) {
        return MessagesFactory.getMessageBuilder().append(s).build();
    }

    public static @NotNull MessageBuilder getMessageBuilder() {
        return MessagesFactory.messageBuilder.clear();
    }

    public static @NotNull Message fromEmbed(@NotNull final MessageEmbed embed) {
        return MessagesFactory.getMessageBuilder().setEmbed(embed).build();
    }

    public static @NotNull EmbedBuilder getEmbedBuilder() {
        return MessagesFactory.embedBuilder.clear();
    }

    @NotNull
    public static Message fromResult(@NotNull Result result, @NotNull CommandListener listener) {
        MessagesProvider messagesProvider = listener.getMessagesProvider();
        EmbedBuilder embedBuilder =
                getEmbedBuilder()
                        .setFooter(messagesProvider.footer())
                        .setTitle(result.getType().getTitle(messagesProvider))
                        .setDescription(result.getMessage())
                        .setColor(result.getType().getColor(listener.getManagerOptions()));
        if (listener.getManagerOptions().isAddThumbnail()) {
            embedBuilder.setThumbnail(listener.getManager().getJda().getSelfUser().getAvatarUrl());
        }
        return fromEmbed(embedBuilder.build());
    }
}

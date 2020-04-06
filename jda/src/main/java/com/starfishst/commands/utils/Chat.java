package com.starfishst.commands.utils;

import com.starfishst.commands.context.CommandContext;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Chat {

    public static void sendOrDelete(
            @NotNull final MessageChannel channel,
            @NotNull final Message message,
            @Nullable final Consumer<Message> success,
            final int delay,
            @NotNull final TimeUnit unit) {
        if (success != null) {
            channel.sendMessage(message).queue(success);
        } else {
            channel.sendMessage(message).queue(msg -> msg.delete().queueAfter(delay, unit));
        }
    }

    public static void send(
            @NotNull final MessageChannel channel,
            @NotNull final Message message,
            @Nullable final Consumer<Message> success) {
        if (success != null) {
            channel.sendMessage(message).queue(success);
        } else {
            channel.sendMessage(message).queue();
        }
    }

    public static void send(@NotNull final MessageChannel channel, @NotNull final Message message) {
        Chat.send(channel, message, null);
    }

    public static void send(
            @NotNull final CommandContext context,
            @NotNull final Message message,
            @Nullable final Consumer<Message> success) {
        Chat.send(context.getChannel(), message, success);
    }

    public static void send(@NotNull final CommandContext context, @NotNull final Message message) {
        Chat.send(context, message, null);
    }

    public static void send(
            @NotNull final MessageChannel channel,
            @NotNull final String message,
            @Nullable final Consumer<Message> success) {
        Chat.send(channel, MessagesFactory.fromString(message), success);
    }

    public static void send(@NotNull final MessageChannel channel, @NotNull final String message) {
        Chat.send(channel, message, null);
    }

    public static void andDelete(
            @NotNull final CommandContext context, @NotNull final String string) {
        Chat.andDelete(context.getChannel(), string);
    }

    public static void andDelete(@NotNull final TextChannel channel, @NotNull final String string) {
        channel.sendMessage(string).queue();
    }

    public static void andDelete(
            @NotNull final MessageChannel channel, @NotNull final String string) {
        final MessageAction action = channel.sendMessage(string);
        action.queue();
    }
}

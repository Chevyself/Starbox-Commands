package com.starfishst.commands.context;

import com.starfishst.core.context.ICommandContext;
import com.starfishst.core.utils.Strings;

import java.util.Arrays;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This context is used for every command {@link User being the sender}
 */
public class CommandContext implements ICommandContext<User> {

    @NotNull
    private final Message message;
    @NotNull
    private final User sender;
    @NotNull
    private final MessageChannel channel;
    @NotNull
    private final MessageReceivedEvent event;
    @NotNull
    private String[] strings;

    public CommandContext(
            @NotNull Message message,
            @NotNull User sender,
            @NotNull String[] args,
            @NotNull MessageChannel channel,
            @NotNull MessageReceivedEvent event) {
        this.message = message;
        this.sender = sender;
        this.strings = args;
        this.channel = channel;
        this.event = event;
    }

    @NotNull
    @Override
    public User getSender() {
        return sender;
    }

    @NotNull
    @Override
    public String getString() {
        return Strings.fromArray(strings);
    }

    @NotNull
    @Override
    public String[] getStrings() {
        return strings;
    }

    /**
     * This method is mostly used when the command is a child to remove the child alias.
     *
     * @param strings the new strings
     */
    public void setStrings(@NotNull String[] strings) {
        this.strings = strings;
    }

    @NotNull
    public Message getMessage() {
        return message;
    }

    @NotNull
    public MessageChannel getChannel() {
        return channel;
    }

    @NotNull
    public MessageReceivedEvent getEvent() {
        return event;
    }

    @Override
    public boolean hasFlag(@NotNull String flag) {
        for (String string : this.strings) {
            if (string.equalsIgnoreCase(flag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "CommandContext{"
                + "message="
                + message
                + ", sender="
                + sender
                + ", strings="
                + Arrays.toString(strings)
                + ", channel="
                + channel
                + ", event="
                + event
                + '}';
    }
}

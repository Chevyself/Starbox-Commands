package com.starfishst.commands.context;

import java.util.Objects;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This context is used when the command is executed inside of a guild the context is still a {@link
 * User} but you can also get the {@link Member}
 */
public class GuildCommandContext extends CommandContext {

    @NotNull
    private final Member member;
    @NotNull
    private final Guild guild;

    public GuildCommandContext(
            @NotNull Message message,
            @NotNull User sender,
            @NotNull String[] args,
            @NotNull MessageChannel channel,
            @NotNull MessageReceivedEvent event) {
        super(message, sender, args, channel, event);
        member =
                Objects.requireNonNull(
                        message.getMember(), "Guild command context must have a valid member");
        guild = message.getGuild();
    }

    @NotNull
    public Member getMember() {
        return member;
    }

    @NotNull
    public Guild getGuild() {
        return guild;
    }

    @Override
    public String toString() {
        return "GuildCommandContext{" + "member=" + member + ", guild=" + guild + '}';
    }
}

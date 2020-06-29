package com.starfishst.commands.utils.responsive.controller;

import com.starfishst.commands.utils.responsive.ReactionResponse;
import com.starfishst.commands.utils.responsive.ResponsiveMessage;
import com.starfishst.core.utils.Atomic;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

/**
 * The controller to use the responsive messages
 */
public interface ResponsiveMessageController {

    default void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (!event.getUser().isBot() || event.getUser().isBot() && acceptBots()){
            ResponsiveMessage responsiveMessage = getResponsiveMessage(event.getMessageIdLong());
            if (responsiveMessage != null) {
                Set<ReactionResponse> reactions = responsiveMessage.getReactions(this.unicodeFromReaction(event));
                if (!reactions.isEmpty()) {
                    Atomic<Boolean> removed = new Atomic<>(false);
                    reactions.forEach(reaction -> {
                        reaction.onReaction(event);
                        if (reaction.removeReaction() && !removed.get()){
                            removed.set(true);
                        }
                    });
                }
            }
        }
    }

    /**
     * Get the responsive message matching the id
     *
     * @param messageId the id to match
     * @return the message if found else null
     */
    default ResponsiveMessage getResponsiveMessage(long messageId) {
        return getResponsiveMessages().stream().filter(message -> message.getId() == messageId).findFirst().orElse(null);
    }

    /**
     * Get the unicode from a reaction event
     *
     * @param event the event where the reaction was added
     * @return the unicode
     */
    @NotNull
    default String unicodeFromReaction(GuildMessageReactionAddEvent event) {
        return event.getReactionEmote().toString().replace("RE:","");
    }

    /**
     * Whether or not bots can use this responsive message
     *
     * @return true if they can
     */
    boolean acceptBots();

    /**
     * Get the responsive messages used in this controller
     *
     * @return the responsive messages
     */
    @NotNull
    Collection<ResponsiveMessage> getResponsiveMessages();

}

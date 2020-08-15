package com.starfishst.commands.utils.responsive.controller;

import com.starfishst.commands.utils.responsive.ReactionResponse;
import com.starfishst.commands.utils.responsive.ResponsiveMessage;
import com.starfishst.core.utils.Atomic;
import java.util.Collection;
import java.util.Set;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.jetbrains.annotations.NotNull;

/** The controller to use the responsive messages */
public interface ResponsiveMessageController {

  default void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
    if (!event.getUser().isBot() || event.getUser().isBot() && acceptBots()) {
      ResponsiveMessage responsiveMessage =
          getResponsiveMessage(event.getGuild(), event.getMessageIdLong());
      if (responsiveMessage != null) {
        Set<ReactionResponse> reactions =
            responsiveMessage.getReactions(this.unicodeFromReaction(event));
        if (!reactions.isEmpty()) {
          Atomic<Boolean> removed = new Atomic<>(false);
          reactions.forEach(
              reaction -> {
                reaction.onReaction(event);
                if (reaction.removeReaction() && !removed.get()) {
                  event.getReaction().removeReaction(event.getUser()).queue();
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
   * @param guild the guild to get the responsive message
   * @param messageId the id to match
   * @return the message if found else null
   */
  default ResponsiveMessage getResponsiveMessage(Guild guild, long messageId) {
    return getResponsiveMessages(guild).stream()
        .filter(message -> message != null && message.getId() == messageId)
        .findFirst()
        .orElse(null);
  }

  /**
   * Get the unicode from a reaction event
   *
   * @param event the event where the reaction was added
   * @return the unicode
   */
  @NotNull
  default String unicodeFromReaction(GuildMessageReactionAddEvent event) {
    return event.getReactionEmote().toString().replace("RE:", "");
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
   * @param guild the guild that requires the messages
   * @return the responsive messages
   */
  @NotNull
  Collection<ResponsiveMessage> getResponsiveMessages(@NotNull Guild guild);
}

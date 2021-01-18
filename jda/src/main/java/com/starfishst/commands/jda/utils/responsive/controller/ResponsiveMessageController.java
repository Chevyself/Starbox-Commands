package com.starfishst.commands.jda.utils.responsive.controller;

import com.starfishst.commands.jda.utils.responsive.ReactionResponse;
import com.starfishst.commands.jda.utils.responsive.ResponsiveMessage;
import java.util.Collection;
import java.util.Set;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

/** The controller to use the responsive messages */
public interface ResponsiveMessageController {

  /**
   * Listen for the reaction being added to a message
   *
   * @param event the event of a reaction being added to a message
   */
  default void onMessageReactionAdd(MessageReactionAddEvent event) {
    if (event.getUser() != null
        && (!event.getUser().isBot() || event.getUser().isBot() && acceptBots())) {
      ResponsiveMessage responsiveMessage =
          this.getResponsiveMessage(event.getGuild(), event.getMessageIdLong());
      if (responsiveMessage != null) {
        Set<ReactionResponse> reactions =
            responsiveMessage.getReactions(
                this.getIdentificationFromReaction(event.getReactionEmote()));
        if (!reactions.isEmpty()) {
          boolean removed = false;
          for (ReactionResponse reaction : reactions) {
            boolean remove = reaction.onReaction(event);
            if (remove && !removed) {
              event.getReaction().removeReaction(event.getUser()).queue();
              removed = true;
            }
          }
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
    for (ResponsiveMessage message : this.getResponsiveMessages(guild)) {
      if (message != null && message.getId() == messageId) {
        return message;
      }
    }
    return null;
  }

  /**
   * Get the unicode or the name of the emote from a reaction event
   *
   * @param emote the emote of the reaction that was added
   * @return the unicode
   */
  @NonNull
  default String getIdentificationFromReaction(MessageReaction.ReactionEmote emote) {
    if (emote.isEmote()) {
      return emote.getEmote().getName();
    } else {
      return emote.toString().replace("RE:", "");
    }
  }

  /**
   * Remove the message from certain guild. This will make that the controller will not listen to it
   *
   * @param guild the guild where the message is from
   * @param message the message to remove
   */
  default void removeMessage(Guild guild, @NonNull ResponsiveMessage message) {
    this.getResponsiveMessages(guild).remove(message);
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
  @NonNull
  Collection<ResponsiveMessage> getResponsiveMessages(Guild guild);
}

package me.googas.commands.jda.utils.responsive;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;

/** A responsive message is created for the user to react to the message with an. */
public interface ResponsiveMessage {

  /**
   * Get the reactions of the message matching an unicode.
   *
   * @return the reactions
   * @param unicode the unicode to match
   */
  @NonNull
  default Set<ReactionResponse> getReactions(@NonNull String unicode) {
    Set<ReactionResponse> reactions = new HashSet<>();
    for (ReactionResponse reaction : this.getReactions()) {
      if (reaction.getUnicode().equalsIgnoreCase("any")
          || reaction.getUnicode().equalsIgnoreCase(unicode)) {
        reactions.add(reaction);
      }
    }
    return reactions;
  }

  /**
   * Add a reaction response to the set.
   *
   * @param response the reaction response to add to the set
   */
  default void addReactionResponse(@NonNull ReactionResponse response) {
    this.getReactions().add(response);
  }

  /**
   * Add a reaction response to the set.
   *
   * @param response the reaction response to add to the set
   * @param message the message to add the reaction
   */
  default void addReactionResponse(@NonNull ReactionResponse response, @NonNull Message message) {
    this.addReactionResponse(response);
    String unicode = response.getUnicode();
    if (unicode.startsWith("U+") || unicode.startsWith("u+")) {
      message.addReaction(unicode).queue();
    } else {
      List<Emote> emotes = message.getGuild().getEmotesByName(unicode, true);
      if (!emotes.isEmpty()) {
        for (Emote emote : emotes) {
          message.addReaction(emote).queue();
        }
      } else {
        throw new IllegalStateException("There's no emotes with the name " + unicode);
      }
    }
  }

  /**
   * Get the id of the message.
   *
   * @return the id
   */
  long getId();

  /**
   * Get the reactions of the message.
   *
   * @return the reactions
   */
  @NonNull
  Set<ReactionResponse> getReactions();
}

package com.starfishst.jda.utils.responsive;

import java.util.HashSet;
import java.util.Set;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

/** A responsive message is created for the user to react to the message with an */
public interface ResponsiveMessage {

  /**
   * Get the reactions of the message matching an unicode
   *
   * @return the reactions
   * @param unicode the unicode to match
   */
  @NotNull
  default Set<ReactionResponse> getReactions(@NotNull String unicode) {
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
   * Add a reaction response to the set
   *
   * @param response the reaction response to add to the set
   */
  default void addReactionResponse(@NotNull ReactionResponse response) {
    this.getReactions().add(response);
  }

  /**
   * Add a reaction response to the set
   *
   * @param response the reaction response to add to the set
   * @param message the message to add the reaction
   */
  default void addReactionResponse(@NotNull ReactionResponse response, @NotNull Message message) {
    this.addReactionResponse(response);
    message.addReaction(response.getUnicode()).queue();
  }

  /**
   * Get the id of the message
   *
   * @return the id
   */
  long getId();

  /**
   * Get the reactions of the message
   *
   * @return the reactions
   */
  @NotNull
  Set<ReactionResponse> getReactions();
}

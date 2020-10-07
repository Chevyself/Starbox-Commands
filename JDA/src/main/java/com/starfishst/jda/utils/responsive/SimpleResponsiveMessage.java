package com.starfishst.jda.utils.responsive;

import java.util.Set;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public class SimpleResponsiveMessage implements ResponsiveMessage {

  /** The set of reactions to which this message will respond */
  @NotNull protected final transient Set<ReactionResponse> reactions;
  /** The id of this message */
  private final long id;

  /**
   * Create the responsive message. This constructor should be used when creating the responsive
   * message from a configuration
   *
   * @param id the id of the responsive message
   * @param reactions the reactions to add in the message
   */
  public SimpleResponsiveMessage(long id, @NotNull Set<ReactionResponse> reactions) {
    this.id = id;
    this.reactions = reactions;
  }

  /**
   * Create the responsive message. This constructor should be used in a message that was already
   * sent
   *
   * @param message the message to make the responsive message
   * @param reactions the reactions to use
   */
  public SimpleResponsiveMessage(
      @NotNull Message message, @NotNull Set<ReactionResponse> reactions) {
    this(message.getIdLong(), reactions);
    for (ReactionResponse reaction : this.reactions) {
      if (!reaction.getUnicode().equalsIgnoreCase("any")) {
        message.addReaction(reaction.getUnicode()).queue();
      }
    }
  }

  /**
   * Get the id of the message
   *
   * @return the id
   */
  public long getId() {
    return id;
  }

  /**
   * Get the reactions of the message
   *
   * @return the reactions
   */
  @NotNull
  public Set<ReactionResponse> getReactions() {
    return reactions;
  }
}

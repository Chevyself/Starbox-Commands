package me.googas.commands.jda.utils.responsive;

import java.util.Set;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Message;

public class StarboxResponsiveMessage implements ResponsiveMessage {

  /** The set of reactions to which this message will respond */
  @NonNull protected final Set<ReactionResponse> reactions;
  /** The id of this message */
  private final long id;

  /**
   * Create the responsive message. This constructor should be used when creating the responsive
   * message from a configuration
   *
   * @param id the id of the responsive message
   * @param reactions the reactions to add in the message
   */
  public StarboxResponsiveMessage(long id, @NonNull Set<ReactionResponse> reactions) {
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
  public StarboxResponsiveMessage(
      @NonNull Message message, @NonNull Set<ReactionResponse> reactions) {
    this(message.getIdLong(), reactions);
    for (ReactionResponse reaction : this.reactions) {
      if (!reaction.getUnicode().equalsIgnoreCase("any")) {
        message.addReaction(reaction.getUnicode()).queue();
      }
    }
  }

  @Override
  public long getId() {
    return this.id;
  }

  @NonNull
  @Override
  public Set<ReactionResponse> getReactions() {
    return this.reactions;
  }
}

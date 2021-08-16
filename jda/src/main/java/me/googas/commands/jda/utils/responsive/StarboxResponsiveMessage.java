package me.googas.commands.jda.utils.responsive;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Message;

/** A simple {@link ResponsiveMessage} implementation. */
public class StarboxResponsiveMessage implements ResponsiveMessage {

  /** The set of reactions to which this message will respond. */
  @NonNull protected final Set<ReactionResponse> reactions;
  /** The id of this message. */
  private final long id;

  /**
   * Create the responsive message. This constructor should be used when creating the responsive
   * message from a configuration
   *
   * @param id the id of the responsive message
   * @param reactions the reactions to add in the message
   */
  public StarboxResponsiveMessage(long id, @NonNull Collection<ReactionResponse> reactions) {
    this.id = id;
    this.reactions = new HashSet<>(reactions);
  }

  /**
   * Create the responsive message. This method should be used in a message that was already sent
   *
   * @param message the message to make the responsive message
   * @param reactions the reactions to use
   * @return the new responsive message
   */
  @NonNull
  public static StarboxResponsiveMessage using(
      @NonNull Message message, @NonNull Collection<ReactionResponse> reactions) {
    StarboxResponsiveMessage responsiveMessage =
        new StarboxResponsiveMessage(message.getIdLong(), reactions);
    reactions.forEach(
        reaction ->
            reaction.getUnicode().ifPresent(unicode -> message.addReaction(unicode).queue()));
    return responsiveMessage;
  }

  @Override
  public @NonNull Set<ReactionResponse> getReactions(@NonNull String unicode) {
    return this.reactions.stream()
        .filter(reaction -> reaction.hasUnicode(unicode))
        .collect(Collectors.toSet());
  }

  @Override
  public @NonNull StarboxResponsiveMessage addReactionResponse(@NonNull ReactionResponse response) {
    this.reactions.add(response);
    return this;
  }

  @Override
  public long getId() {
    return this.id;
  }
}

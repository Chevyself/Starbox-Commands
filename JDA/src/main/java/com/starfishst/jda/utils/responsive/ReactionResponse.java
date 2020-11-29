package com.starfishst.jda.utils.responsive;

import lombok.NonNull;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

/** This class is the one that executes an action when the unicode matches */
public interface ReactionResponse {

  /**
   * Whether or not the reaction should be removed
   *
   * @return true if it should be removed
   * @deprecated you can now decide whether to remove the reaction in {@link
   *     #onReaction(MessageReactionAddEvent)}
   */
  boolean removeReaction();

  /**
   * If unicode matches this is the action to run
   *
   * @param event the reaction event
   * @return whether to remove the reaction. true to remove it false otherwise
   */
  boolean onReaction(@NonNull MessageReactionAddEvent event);

  /**
   * If unicode matches this is the action to run
   *
   * @param event the reaction event
   */
  default void onReactionRemoved(@NonNull MessageReactionRemoveEvent event) {}

    /**
   * The unicode to match and run the action
   *
   * @return the unicode. It can also be 'any' to accept any kind of emote
   */
  @NonNull
  String getUnicode();
}
